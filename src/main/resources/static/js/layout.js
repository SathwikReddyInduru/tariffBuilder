function getState() {
    return JSON.parse(sessionStorage.getItem('state') ||
        '{"s2":null,"s3":[],"s4":[],"price":"","publicityCode":"","endDate":"","isCorporate":false}');
}

function saveState(state) {
    sessionStorage.setItem('state', JSON.stringify(state));
}

window.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('configName');
    if (!input) return;

    // Restore saved name
    const savedName = sessionStorage.getItem('configName') || '';
    input.value = savedName;

    // Save on every keystroke so navigation never loses it
    input.addEventListener('input', () => {
        sessionStorage.setItem('configName', input.value);
    });
});

// ── Step access guard ──
function checkStepAccess(step) {
    const type = sessionStorage.getItem('pkgType');
    if (step > 1 && !type) {
        alert("Please select PREPAID or POSTPAID in Step 1");
        return false;
    }
    return true;
}

// ── Module switcher ──
function switchModule(m) {
    document.getElementById('m-pkg').classList.toggle('active', m === 'pkg');
    document.getElementById('m-reload').classList.toggle('active', m === 'reload');

    const isPkg = m === 'pkg';
    document.getElementById('stepRail').style.display = isPkg ? 'flex' : 'none';
    document.getElementById('sidebar').style.display = isPkg ? 'flex' : 'none';
    document.getElementById('footerActions').style.display = isPkg ? 'flex' : 'none';

    const stepStage = document.querySelector('.stage:not(#reloadStage)');
    if (stepStage) stepStage.classList.toggle('hidden', !isPkg);
    document.getElementById('reloadStage').classList.toggle('hidden', isPkg);
}

// ── Save eReload ──
async function saveReload() {
    const payload = {
        rechargeId: document.getElementById('rechargeId').value,
        rechargeType: document.getElementById('rechargeType').value,
        mrp: document.getElementById('mrp').value,
        airTime: document.getElementById('airTime').value,
        validityDays: document.getElementById('validityDays').value,
        gracePeriod1: document.getElementById('gracePeriod1').value,
        gracePeriod2: document.getElementById('gracePeriod2').value,
    };
    if (!payload.rechargeId) { alert('Enter Recharge ID'); return; }
    try {
        const res = await fetch('/api/v1/saveReload', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (res.ok) alert('eReload Configuration Saved.');
    } catch (e) { alert('Backend error.'); }
}

// ── Save package config ──
async function saveConfiguration() {
    const name = document.getElementById('configName').value;
    if (!name) { alert('Enter Configuration Name'); return; }

    const state = getState();
    const pkgType = sessionStorage.getItem('pkgType') || '';
    const pkgSubType = sessionStorage.getItem('pkgSubType') || '';

    const payload = { name, id: Date.now(), pkgType, pkgSubType, state };

    try {
        const res = await fetch('/api/v1/saveTariff', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (res.ok) alert('Package Saved.');
    } catch (e) { alert('Backend error.'); }
}

// ── Hierarchy modal ──
function viewTree() {
    const state = getState();
    const name = document.getElementById('configName').value || 'Unnamed Package';
    const type = sessionStorage.getItem('pkgType') || '';
    const sub = sessionStorage.getItem('pkgSubType') || 'NORMAL';

    document.getElementById('treeName').textContent = name;
    document.getElementById('treeMeta').textContent = `${type ? type + ' | ' : ''}${sub} | ${state.isCorporate ? 'Corporate' : 'Retail'}`;
    document.getElementById('treeMain').textContent = `📦 Main Service Plan: ${state.s2 ? state.s2.name : 'None'}`;
    document.getElementById('treeDatp').textContent = `➕ DATP Components: ${(state.s3 || []).length} items`;
    document.getElementById('treeAatp').textContent = `🛒 AATP Components: ${(state.s4 || []).length} items`;
    document.getElementById('treeCharge').innerHTML = `<b>Charge: RM ${state.price || '0.00'}</b> | <b>Ends: ${state.endDate || 'Permanent'}</b>`;

    document.getElementById('treeModal').classList.add('open');
}

function closeTree() {
    document.getElementById('treeModal').classList.remove('open');
}

document.addEventListener('keydown', e => {
    if (e.key === 'Escape') closeTree();
});

// ── Logout ──
function logout() { window.location.href = '/loginform'; }