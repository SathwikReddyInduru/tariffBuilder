// step3.js — Step 3 (Default ATPs / DATP)
// Writes selected items to state.s3[] in sessionStorage

window.addEventListener('DOMContentLoaded', () => {
    // Restore saved DATP items
    const state = getState();
    state.s3.forEach(item => renderCard(item));

    // Restore pill selections
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

    const mockData = { VOICE: ['Voice ATP 1', 'Voice ATP 2'], SMS: ['SMS ATP 1'], DATA: ['Data ATP 1'] };
    let items = [];
    selectedSvcs.forEach(svc => { items = items.concat(mockData[svc] || []); });

    // Exclude items already added
    const state = getState();
    const addedIds = state.s3.map(i => i.id);

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
    // Prevent duplicates
    if (state.s3.find(i => i.id === itemName)) return;
    const item = { id: itemName, name: itemName };
    state.s3.push(item);    // ← WRITES to state.s3[]
    saveState(state);
    renderCard(item);
    updateSidebar();        // refresh to remove added item from sidebar
}

function renderCard(item) {
    const container = document.getElementById('dropArea');
    const card = document.createElement('div');
    card.className = 'service-card';
    card.id = `card-s3-${item.id}`;
    card.innerHTML = `
        <div style="display:flex; justify-content:space-between; border-bottom:1px solid #eee; padding-bottom:10px;">
            <b class="service-title">${item.name}</b>
            <span onclick="removeItem('${item.id}')" style="color:red; cursor:pointer; font-weight:800;">✕</span>
        </div>`;
    container.appendChild(card);
}

function removeItem(id) {
    const state = getState();
    state.s3 = state.s3.filter(i => i.id !== id);  // ← REMOVES from state.s3[]
    saveState(state);
    const card = document.getElementById(`card-s3-${id}`);
    if (card) card.remove();
    updateSidebar();
}