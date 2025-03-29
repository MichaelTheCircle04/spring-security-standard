var csrfToken = document.querySelector('meta[name="csrf-token"]').content;
var csrfHeader = document.querySelector('meta[name="csrf-header"]').content;

function login() {
    window.location = "http://localhost:8080/login";
}

function cleanForm(flag) {

    var form = document.getElementById("userData");

    if (flag) {
        form.elements.username.value = "";
    }
    
    form.elements.password.value = "";
    form.elements.passwordAgain.value = "";
}

function submitForm() {

    var form = document.getElementById("userData");

    if (form.elements.password.value !== form.elements.passwordAgain.value) {
        cleanForm(false);
        document.getElementById("message").innerHTML = "Passwords didn't match";
        document.getElementById("message").style = "color: red";
        return;
    }

    var data = {
        username: form.elements.username.value,
        password: form.elements.password.value,
        roles: ["ROLE_USER"]
    };

    var jsonData = JSON.stringify(data);
    headers = { "Content-type": "application/json" };
    headers[csrfHeader] = csrfToken;

    fetch(form.action, {
        method: "POST",
        headers: headers,
        body: jsonData
    })
        .then(resp => {
            if (resp.status === 201) {
                document.getElementById("message").innerHTML = "Registered";
                document.getElementById("message").style = "color: green";
                cleanForm(true);
                return Promise.reject("Done");
            }
            return Promise.resolve(resp.json);
        })
        .then(resp => {
            document.getElementById("message").innerHTML = resp.exception;
            document.getElementById("message").style = "color: red";
            cleanForm(true);
        }).catch(message => console.log(message));
}