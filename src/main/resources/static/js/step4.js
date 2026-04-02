// step4.js — Step 4 (Additional ATPs / AATP)
// Writes selected items to state.s4[] in sessionStorage
// Same pattern as step3.js but targets state.s4

window.addEventListener('DOMContentLoaded', () => {
    const state = getState();
    state.s4.forEach(item => renderCard(item));

    const saved = JSON.parse(sessionStorage.getItem('selectedSvcs') || '[]');
    saved.forEach(svc => {
        const pill = document.querySelector(`.svc-pill[data-svc="${svc}"]`);
        if (pill) pill.classList.add('active');
        if (!selectedSvcs.includes(svc)) selectedSvcs.push(svc);
    });
    if (saved.length) updateSidebar();
});

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
    updateSidebar();
}

function updateSidebar() {
    const list = document.getElementById('comp-list');
    if (!selectedSvcs.length) { list.innerHTML = ''; return; }

    const mockData = { VOICE: ['Voice Add-on 1', 'Voice Add-on 2'], SMS: ['SMS Add-on 1'], DATA: ['Data Add-on 1'] };
    let items = [];
    selectedSvcs.forEach(svc => { items = items.concat(mockData[svc] || []); });

    const state = getState();
    const addedIds = state.s4.map(i => i.id);

    list.innerHTML = items
        .filter(item => !addedIds.includes(item))
        .map(item => `
            <div class="draggable-item" onclick="addToCenter('${item}')">
                <b>${item}</b>
            </div>
        `).join('');
}

function addToCenter(itemName) {
    const state = getState();
    if (state.s4.find(i => i.id === itemName)) return;
    const item = { id: itemName, name: itemName };
    state.s4.push(item);    // ← WRITES to state.s4[]
    saveState(state);
    renderCard(item);
    updateSidebar();
}

function renderCard(item) {
    const container = document.getElementById('dropArea');
    const card = document.createElement('div');
    card.className = 'service-card';
    card.id = `card-s4-${item.id}`;
    card.innerHTML = `
        <div style="display:flex; justify-content:space-between; border-bottom:1px solid #eee; padding-bottom:10px;">
            <b class="service-title">${item.name}</b>
            <span onclick="removeItem('${item.id}')" style="color:red; cursor:pointer; font-weight:800;">✕</span>
        </div>`;
    container.appendChild(card);
}

function removeItem(id) {
    const state = getState();
    state.s4 = state.s4.filter(i => i.id !== id);  // ← REMOVES from state.s4[]
    saveState(state);
    const card = document.getElementById(`card-s4-${id}`);
    if (card) card.remove();
    updateSidebar();
}