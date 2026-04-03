// ---------- Step3.js ----------

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
 
// ---------- SIDEBAR (DB CALL) ----------

function refreshSidebar() {
 
    const list = document.getElementById('comp-list');
 
    const svcMap = { VOICE: '1', SMS: '2', DATA: '3' };
 
    const types = selectedSvcs.map(s => svcMap[s]).sort().join(",");
 
    if (!types) {

        list.innerHTML = '';

        return;

    }
 
    list.innerHTML = "Loading...";
 
    fetch(`/builder/step3/filter?types=${types}`)

        .then(res => res.json())

        .then(data => {
 
            if (!data || !data.length) {

                list.innerHTML = '<p>No data</p>';

                return;

            }
 
            list.innerHTML = data.map(plan => `
<div class="draggable-item"

                     onclick="addToCenter('${plan.servicePackageId}', '${plan.servicePackageName}')">

                    ${plan.servicePackageName}
</div>

            `).join('');

        })

        .catch(err => {

            console.error("Fetch error:", err);

            list.innerHTML = '<p>Error loading data</p>';

        });

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
 