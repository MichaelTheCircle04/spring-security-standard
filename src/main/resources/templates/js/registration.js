function submitForm(event) {
    event.preventDefault();
    var form = document.getElementById('userData');
    if (form.elements.password.value !== form.elements.passwordAgain.value) {
        form.elements.password.value = '';
        form.elements.passwordAgain.value = '';
        return false;
    }
    var data = {
        username: form.elements.username.value,
        password: form.elements.password.value + ':' + form.elements.passwordAgain.value,
        roles: ['USER']
        };
    var jsonData = JSON.stringify(data);
    fetch(form.action, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json'
        },
        body: jsonData
    });
}

