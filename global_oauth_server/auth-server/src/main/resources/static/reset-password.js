function submitForm(evt) {
    let errorMessage = document.getElementById('errorMessage');
    let restoreForm = document.restore;
    if (!restoreForm.restorePass.value || !restoreForm.restoreConfPass.value) {
        evt.preventDefault();
        errorMessage.style.display = 'block';
        errorMessage.innerHTML = 'Fields are required';
    } else {
        if (restoreForm.restorePass.value !== restoreForm.restoreConfPass.value) {
            evt.preventDefault();
            errorMessage.style.display = 'block';
            errorMessage.innerHTML = 'Password is not confirmed';
        } else {
            errorMessage.style.display = 'none';
        }
    }
}
