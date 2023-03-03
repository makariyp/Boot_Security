$(function () {
    fetch("http://localhost:8080/api/user/user", {method: "GET"})
        .then((response) => response.json())
        .then((user) => {
            console.log("Success:", user);
            $("#currentUser").html(user.name + ' с правами: ' + user.roles.reduce((s, i) => s + ' ' + i.name.replaceAll("ROLE_", ""), ''));
            $("#userName").html(user.name);
            $("#userAge").html(user.age);
            $("#userEmail").html(user.email);
            $("#userId").html(user.id);
            $("#userRoles").html(user.roles.reduce((s, i) => s + ' ' + i.name.replaceAll("ROLE_", ""), ''));
        })
        .catch((error) => {
            console.error("Error:", error);
        });
    $("#currentUser").html();
    updateTable();
})