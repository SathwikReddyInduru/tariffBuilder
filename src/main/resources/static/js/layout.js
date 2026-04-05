function getState() {

    const defaultState = {
        s2: [],
        s3: [],
        s4: [],
        price: "",
        publicityCode: "",
        endDate: "",
        isCorporate: false
    };

    const stored = sessionStorage.getItem('state');

    return stored ? JSON.parse(stored) : defaultState;
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
function checkStepAccess(targetStep) {

    const currentPath = window.location.pathname;

    // detect current step
    if (currentPath.includes(`step${targetStep}`)) {
        return true;
    }

    if (targetStep === 1) {
        return true;
    }

    const pkgType = sessionStorage.getItem('pkgType');
    const state = JSON.parse(sessionStorage.getItem('state') || '{}');

    // ---------- STEP 1 ----------
    if (!pkgType) {
        alert("Please select PREPAID or POSTPAID in Step 1");
        return false;
    }

    // ---------- STEP 2 ----------
    const hasStep2Data =
        state.s2 &&
        Array.isArray(state.s2) &&
        state.s2.length > 0;

    if (targetStep > 2 && !hasStep2Data) {
        alert("Please select Service Plan in Step 2");
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

//Logout
function toggleUserMenu() {
    const dropdown = document.getElementById("userDropdown");
    dropdown.classList.toggle("active");
}

// close when clicking outside
document.addEventListener("click", function (e) {
    const menu = document.querySelector(".user-menu");
    if (!menu.contains(e.target)) {
        document.getElementById("userDropdown").classList.remove("active");
    }
});

// ── Save package config ──
async function saveConfiguration() {

    const configName = document.getElementById("configName").value;

    if (!configName) {
        alert("Enter Configuration Name");
        return;
    }

    const state = JSON.parse(sessionStorage.getItem("state"));

    if (!state?.s2?.length) {
        alert("Step 2 required");
        return;
    }

    if (!state?.s3?.length) {
        alert("Step 3 required");
        return;
    }

    /* Step 3 name → chargeId */
    const step3Name = state.s3[0].name
        .replace(/\s+/g, '_')
        .toUpperCase();

    const chargeId = step3Name + "_PR";

    function formatDateToMMDDYYYY(dateStr) {
        if (!dateStr) return "12/31/2030"; // default

        const [year, month, day] = dateStr.split("-");
        return `${month}/${day}/${year}`;
    }

    const payload =
    {
        networkId: 16,

        username: "USER1",

        packageType:
            sessionStorage.getItem("pkgType"),

        tariffPackCategory:
            sessionStorage.getItem("pkgSubType") || "GENERAL",

        tariffPackageDesc:
            configName,

        endDate: formatDateToMMDDYYYY(state.endDate) ||
            "12/31/2030",

        publicityId:
            state.publicityCode || "DEFAULT_PUB",

        chargeId:
            chargeId,

        isCorporateYn:
            state.isCorporate ? "Y" : "N",

        tariffPlanId:
            Number(state.s2[0].id),

        defaultAtps:
            state.s3.map(item => ({

                servicePackageId:
                    Number(item.id),

                chargeId:
                    chargeId
            })),

        allowedAtps:
            state.s4.map(item => ({

                servicePackageId:
                    Number(item.id),

                chargeId:
                    chargeId
            }))
    };

    console.log("REQUEST", payload);

    try {
        const response =
            await fetch("/saveConfig", {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(payload)
            });

        const result = await response.json();

        console.log("RESPONSE", result);

        if (response.ok) {
            alert("Configuration Saved Successfully");

            clearBuilderSession();
            window.location.href = '/builder/step1';
        }
        else {
            alert(result.error);
        }
    }
    catch (error) {
        console.error(error);

        alert("Server error");
    }
}

function clearBuilderSession() {
    sessionStorage.removeItem('state');
    sessionStorage.removeItem('selectedSvcs_s2');
    sessionStorage.removeItem('selectedSvcs_s3');
    sessionStorage.removeItem('selectedSvcs_s4');
    sessionStorage.removeItem('configName');
    sessionStorage.removeItem('pkgType');
    sessionStorage.removeItem('pkgSubType');
}

// ── Hierarchy modal ──
function viewTree() {
    const state = getState();
    const name = document.getElementById('configName').value || 'Unnamed Package';
    const type = sessionStorage.getItem('pkgType') || '';
    const sub = sessionStorage.getItem('pkgSubType') || 'NORMAL';

    document.getElementById('treeName').textContent = name;
    document.getElementById('treeMeta').textContent = `${type ? type + ' | ' : ''}${sub} | ${state.isCorporate ? 'Corporate' : 'Retail'}`;
    document.getElementById('treeMain').textContent = `📦 Main Service Plan: ${state.s2 && state.s2[0] ? state.s2[0].name : 'None'}`;
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
function logout() {
    sessionStorage.clear();
    window.location.href = '/logout';
}