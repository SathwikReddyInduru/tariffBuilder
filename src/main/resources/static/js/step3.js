// ---------- STATE ----------
function getState() {
    return JSON.parse(sessionStorage.getItem('builderState') || '{"s3":[]}');
}

function saveState(state) {
    sessionStorage.setItem('builderState', JSON.stringify(state));
}

// ---------- INIT ----------
window.addEventListener('DOMContentLoaded', () => {

    const state = getState();

    // restore cards
    state.s3.forEach(item => renderCard(item));

    // restore pills
    const saved = JSON.parse(sessionStorage.getItem('selectedSvcs') || '[]');

    saved.forEach(svc => {
        const pill = document.querySelector(`.svc-pill[data-svc="${svc}"]`);
        if (pill) pill.classList.add('active');
        if (!selectedSvcs.includes(svc)) selectedSvcs.push(svc);
    });

    if (saved.length) refreshSidebar();
});

// ---------- SERVICE SELECTION ----------
let selectedSvcs = [];

function toggleSvc(service, el) {

    if (selectedSvcs.includes(service)) {
        selectedSvcs = selectedSvcs.filter(s => s !== service);
        el.classList.remove('active');
    } else {
        selectedSvcs.push(service);
        el.classList.add('active');
    }

    sessionStorage.setItem('selectedSvcs', JSON.stringify(selectedSvcs));

    refreshSidebar();
}

// ---------- MOCK DATA ----------
const mockData = {
    VOICE: [
        { id: "V1", name: "Voice Pack 1" },
        { id: "V2", name: "Voice Pack 2" }
    ],
    SMS: [
        { id: "S1", name: "SMS Pack 1" }
    ],
    DATA: [
        { id: "D1", name: "Data Pack 1" }
    ]
};

// ---------- SIDEBAR ----------
function refreshSidebar() {

    const list = document.getElementById('comp-list');

    if (!selectedSvcs.length) {
        list.innerHTML = '';
        return;
    }

    let items = [];

    selectedSvcs.forEach(svc => {
        items = items.concat(mockData[svc] || []);
    });

    list.innerHTML = items.map(plan => `
        <div class="draggable-item"
             onclick="addToCenter('${plan.id}', '${plan.name}')">
            ${plan.name}
        </div>
    `).join('');
}

// ---------- ADD ----------
function addToCenter(id, name) {

    const state = getState();

    // prevent duplicate
    if (state.s3.find(i => i.id === id)) return;

    const item = {
        id: id,
        name: name,
        validity: "Monthly",
        renewal: "No"
    };

    state.s3.push(item);
    saveState(state);

    renderCard(item);
}

// ---------- RENDER ----------
function renderCard(item) {

    const container = document.getElementById('dropArea');

    const card = document.createElement('div');
    card.className = 'service-card';
    card.id = `card-s3-${item.id}`;

    card.innerHTML = `
        <div style="display:flex; justify-content:space-between; margin-bottom:10px;">
            <b>${item.name}</b>
            <span onclick="removeItem('${item.id}')" style="color:red; cursor:pointer;">✕</span>
        </div>

        <div class="card-grid">

            <!-- BASIC -->
            <div class="card-field">
                <label>VALIDITY</label>
                <select onchange="updateField('${item.id}', 'validity', this.value)">
                    <option ${item.validity === 'Monthly' ? 'selected' : ''}>Monthly</option>
                    <option ${item.validity === 'Weekly' ? 'selected' : ''}>Weekly</option>
                </select>
            </div>

            <div class="card-field">
                <label>MIDNIGHT EXPIRY</label>
                <select>
                    <option>No</option>
                    <option>Yes</option>
                </select>
            </div>

            <div class="card-field">
                <label>AUTO RENEWAL</label>
                <select onchange="handleRenewalChange('${item.id}', this.value)">
                    <option ${item.renewal === 'No' ? 'selected' : ''}>No</option>
                    <option ${item.renewal === 'Yes' ? 'selected' : ''}>Yes</option>
                </select>
            </div>

            <!-- CONDITIONAL -->
            <div id="renewal-${item.id}" style="display:${item.renewal === 'Yes' ? 'contents' : 'none'};">

                <div class="card-field">
                    <label>RENTAL</label>
                    <input type="number">
                </div>

                <div class="card-field">
                    <label>MAX COUNT</label>
                    <input type="number">
                </div>

                <div class="card-field">
                    <label>FREE CYCLES</label>
                    <input type="number" value="0">
                </div>

            </div>

        </div>
    `;

    container.appendChild(card);
}

// ---------- UPDATE ----------
function updateField(id, key, value) {
    const state = getState();

    const item = state.s3.find(i => i.id === id);

    if (item) {
        item[key] = value;
        saveState(state);
    }
}

// ---------- RENEWAL ----------
function handleRenewalChange(id, value) {

    updateField(id, 'renewal', value);

    const section = document.getElementById(`renewal-${id}`);

    section.style.display = value === 'Yes' ? 'contents' : 'none';
}

// ---------- REMOVE ----------
function removeItem(id) {

    const state = getState();

    state.s3 = state.s3.filter(i => i.id !== id);
    saveState(state);

    document.getElementById(`card-s3-${id}`)?.remove();
}