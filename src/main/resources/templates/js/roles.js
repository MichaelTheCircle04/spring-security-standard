console.log("text")
var csrfToken = document.querySelector('meta[name="csrf-token"]').content;
var header = document.querySelector('meta[name="csrf-header"]').content;

var rows = document.getElementById("roles").querySelectorAll("tr"); //получаем роли из таблицы на html странице

var roles = [];
rows.forEach(r => roles.push(r.querySelector("td").innerHTML)); //помещаем их в массив

roles.forEach(r => document.getElementById(r).checked = true); //отмечаем те, которые есть у пользователя

function reload() { //перезагрузка страницы
    location.reload();
}

function change() {

    var rolesForAdd = [];
    var rolesForDelete = [];

    var checkboxes = document.getElementsByName("userRole");
    checkboxes.forEach(r => { 
        if (r.checked) {
            if (!roles.includes(r.id)) { //если роль отмечена и ее нет у пользователя, включаем в список на добавление
                rolesForAdd.push(r.id);
            }
        } else {
            if (roles.includes(r.id)) { //если роль не отмечена, но она есть у пользователя, включаем в список на удаление
                rolesForDelete.push(r.id);
            }
        }
    });

    if (rolesForAdd.length === 0 && rolesForDelete.length === 0) {
        console.log("Seems like here nothing to change");
        return;
    }

    var data = {
        currentRoles: roles,
        rolesForAdd: rolesForAdd,
        rolesForDelete: rolesForDelete
    };
    
    headers = { "Content-type": "application/json" };
    headers[header] = csrfToken;
    var jsonData = JSON.stringify(data);
    //console.log(jsonData);
    
    fetch("http://localhost:8081/admin/change/role/" + document.getElementById("id").innerHTML, {
        method: "POST",
        headers: headers,
        body: jsonData
    })
        .then(resp => {
            if (resp.ok) {
                document.getElementById("message").innerHTML = "Changed",
                document.getElementById("message").style = "color: green";
                return Promise.reject();
            } 

            return Promise.resolve(resp.json());
        })
        .then(json => {
            document.getElementById("message").innerHTML = json.exception;
            document.getElementById("message").style = "color: red";
        }).catch(message => console.log(message));
}