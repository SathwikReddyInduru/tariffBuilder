let selectedSvcs = [];

function toggleSvc(service, el) {

    // toggle selection
    if (selectedSvcs.includes(service)) {
        selectedSvcs = selectedSvcs.filter(s => s !== service);
        el.classList.remove('active');
    } else {
        selectedSvcs.push(service);
        el.classList.add('active');
    }

    const svcMap = { VOICE: '1', SMS: '2', DATA: '3' };

    // 👉 convert to exact DB format like "1" or "1,2,3"
    const types = selectedSvcs.map(s => svcMap[s]).sort().join(",");

    console.log("Sending:", types);

    const list = document.getElementById('comp-list');
    list.innerHTML = "Loading...";

    fetch(`/builder/step2/filter?types=${types}`)
        .then(res => res.json())
        .then(data => {

            if (!data.length) {
                list.innerHTML = '<p></p>';
                return;
            }

            list.innerHTML = data.map(plan => `
                <div class="draggable-item"
                     onclick="addToMain('${plan.servicePackageId}', '${plan.servicePackageName}')">
                    ${plan.servicePackageName}
                </div>
            `).join('');
        })
        .catch(err => {
            console.error(err);
            list.innerHTML = '<p>Error loading data</p>';
        });
}


function addToMain(id, name) {

    const mainArea = document.getElementById('main-area');

    if (document.querySelector(`[data-dropped-id="${id}"]`)) return;

    const item = document.createElement('div');
    item.className = 'service-card';
    item.dataset.droppedId = id;

    item.innerHTML = `
        <div style = "display:flex; justify-content:space-between; border-bottom:1px solid #eee; padding-bottom:10px;">
            <b class="service-title">${name}</b>
            <span style="color:red; cursor:pointer; font-weight:800;" onclick="this.parentElement.remove()">✕</span>
        </div>
    `;

    mainArea.appendChild(item);
}