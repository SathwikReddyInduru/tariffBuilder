function handleAction(tariffPackageId, status) {
    const message = status === 'A' ? 'Approved' : 'Rejected';

    fetch('/admin/updateStatus', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            tariffPackageId: tariffPackageId, // clean & clear
            status: status
        })
    })
        .then(res => {
            if (res.ok) {
                alert(message);
                document.getElementById('card-' + tariffPackageId).remove();
            } else {
                alert('Error!');
            }
        });
}