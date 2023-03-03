$(function () {
    fetch("http://localhost:8080/api/user/user", {method: "GET"})
        .then((response) => response.json())
        .then((user) => {
            console.log("Success:", user);
            $("#currentUser").html(user.name + ' с правами: ' + user.roles.reduce((s, i) => s + ' ' + i.name.replaceAll("ROLE_", ""), ''));
        })
        .catch((error) => {
            console.error("Error:", error);
        });
    $("#currentUser").html()
    updateTable();
})

function addUser(user) {
    const s = `<tr id="${'tr' + user.id}">
                                   <th>${user.id}</th>
                                   <td id="${'name' + user.id}">${user.name}</td>
                                   <td id="${'age' + user.id}">${user.age}</td>
                                   <td id="${'email' + user.id}">${user.email}</td>
                                   <td id="${'roles' + user.id}">${user.roles.reduce((s, i) => s + i.name.replaceAll("ROLE_", "") + " ", "")}</td>
                                   <td>
                                    <button type="button" class="btn btn-info btnmod" data-bs-toggle="modal"
                                            data-bs-target="#mod"
                                            data-name="${user.name}"
                                            data-id="${user.id}"
                                            data-email="${user.email}"
                                            data-age="${user.age}"
                                            data-roles="${user.roles.reduce((s, i) => s + i.name + ", ", "")}">Редактировать
                                    </button>
                                </td>
                                <td>
                                    <button type="button" class="btn btn-danger btndel" data-bs-toggle="modal"
                                            data-bs-target='#del'
                                            data-name="${user.name}"
                                            data-id="${user.id}"
                                            data-email="${user.email}"
                                            data-age="${user.age}"
                                            data-roles="${user.roles.reduce((s, i) => s + i.name + ", ", "")}">Удалить
                                    </button>
                                </td>
                               </tr>`;
    $(s).appendTo(".tbody");
}

function updateTable() {
    fetch("http://localhost:8080/api/admin/users", {method: "GET"})
        .then((response) => response.json())
        .then((data) => {
            console.log("Success:", data);
            data.forEach(user => {
                addUser(user)
            });
        })
        .catch((error) => {
            console.error("Error:", error);
        });
}

$(".tbody").on('click', '.btnmod',
    function () {
        var id = $(this).attr('data-id');
        var name = $(this).attr('data-name');
        var email = $(this).attr('data-email');
        var age = $(this).attr('data-age');
        var roles = $(this).attr('data-roles');

        $(".id").attr('value', id);
        $(".name").attr('value', name);
        $(".email").attr('value', email);
        $(".age").attr('value', age);

        $(".role").prop('checked', false);
        let role = roles.replaceAll("[", "").replaceAll("]", "").split(", ");
        role.forEach(i => $('#mod' + i).prop('checked', true));
    })

$(".tbody").on('click', '.btndel',
    function () {
        var id = $(this).attr('data-id');
        var name = $(this).attr('data-name');
        var email = $(this).attr('data-email');
        var age = $(this).attr('data-age');
        var roles = $(this).attr('data-roles');

        $(".id").attr('value', id);
        $(".name").attr('value', name);
        $(".email").attr('value', email);
        $(".age").attr('value', age);

        $(".role").prop('checked', false);
        let role = roles.replaceAll("[", "").replaceAll("]", "").split(", ");
        role.forEach(i => $('#del' + i).prop('checked', true));
    })

$("body").on('submit', '#addForm',
    function (e) {
    let formData = new FormData($(this)[0]);
    console.log(...formData)
    formData.delete("roles");
    let roles = new Array();
    this.querySelectorAll('input[name=roles]:checked').forEach(element => {
        roles.push(element.value)
    })
    let obj = {};
    formData.forEach((value, key) => obj[key] = value);
    obj["roles"] = [];
    roles.forEach(i => obj["roles"].push({"id": i}));
    let json = JSON.stringify(obj);
    console.log(json);
    fetch("http://localhost:8080/api/admin/add", {
        method: "POST", // or 'PUT'
        headers: {
            "Content-Type": "application/json",
        },
        body: json,
    })
        .then((response) => response.json())
        .then((user) => {
            console.log("Success:", user);
            fetch("http://localhost:8080/api/admin/user/" + user.id, {method: "GET"})
                .then((response) => response.json())
                .then((data) => {
                    console.log("Success:", data);
                    addUser(data);
                })
                .catch((error) => {
                    console.error("Error:", error);
                });
            e.target.reset();
        })
        .catch((error) => {
            console.error("Error:", error);
        });
})

$("body").on('submit', '#deleteForm',
    function (e) {
    let formData = new FormData($(this)[0]);
    console.log(...formData)
    formData.delete("roles");
    let roles = [];
    this.querySelectorAll('input[name=roles]:checked').forEach(element => {
        roles.push(element.value)
    })
    let obj = {};
    formData.forEach((value, key) => obj[key] = value);
    obj["roles"] = [];
    roles.forEach(i => obj["roles"].push({"id": i}));
    let json = JSON.stringify(obj);
    console.log(json);
    fetch("http://localhost:8080/api/admin/delete", {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
        body: json,
    })
        .then((response) => response.json())
        .then((user) => {
            console.log("Success:", user);
            let tr = document.getElementById('tr' + user.id);
            tr.parentNode.removeChild(tr);
            e.target.reset();
            $("#del").modal('hide');
        })
        .catch((error) => {
            console.error("Error:", error);
        });
})

$("body").on('submit', '#editForm',
    function (e) {
    let formData = new FormData($(this)[0]);
    console.log(...formData)
    formData.delete("roles");
    let roles = [];
    this.querySelectorAll('input[name=roles]:checked').forEach(element => {
        roles.push(element.value)
    })
    let obj = {};
    formData.forEach((value, key) => obj[key] = value);
    obj["roles"] = [];
    roles.forEach(i => obj["roles"].push({"id": i, "name": i == 1 ? "ROLE_USER" : "ROLE_ADMIN"}));
    let json = JSON.stringify(obj);
    console.log(json);
    fetch("http://localhost:8080/api/admin/edit", {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
        },
        body: json,
    })
        .then((response) => response.json())
        .then((user) => {
            console.log("Success:", user);
            let s = `<th>${user.id}</th>
                                   <td id="${'name' + user.id}">${user.name}</td>
                                   <td id="${'age' + user.id}">${user.age}</td>
                                   <td id="${'email' + user.id}">${user.email}</td>
                                   <td id="${'roles' + user.id}">${user.roles.reduce((s, i) => s + i.name.replaceAll("ROLE_", "") + " ", "")}</td>
                                   <td>
                                    <button type="button" class="btn btn-info btnmod" data-bs-toggle="modal"
                                            data-bs-target="#mod"
                                            data-name="${user.name}"
                                            data-id="${user.id}"
                                            data-email="${user.email}"
                                            data-age="${user.age}"
                                            data-roles="${user.roles.reduce((s, i) => s + i.name + ", ", "")}">Редактировать
                                    </button>
                                </td>
                                <td>
                                    <button type="button" class="btn btn-danger btndel" data-bs-toggle="modal"
                                            data-bs-target='#del'
                                            data-name="${user.name}"
                                            data-id="${user.id}"
                                            data-email="${user.email}"
                                            data-age="${user.age}"
                                            data-roles="${user.roles.reduce((s, i) => s + i.name + ", ", "")}">Удалить
                                    </button>
                                </td>`;
            $("#tr" + user.id).html(s);
            e.target.reset();
            $("#mod").modal('hide');
        })
        .catch((error) => {
            console.error("Error:", error);
        });
});