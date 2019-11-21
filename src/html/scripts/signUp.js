let signUpMenuButton = document.getElementById("signUpMenuButton");

signUpMenuButton.onclick = () => {
    document.getElementById("signIn").style.display = "none";
    document.getElementById("signUp").style.display = "block";

}


let getStart = document.getElementById("getStart");

getStart.onclick = () => {
    var nameBox = document.getElementById("usernameSignUp");
    var name = nameBox.value;
    var passwordBox = document.getElementById("passwordSignup");
    var password = passwordBox.value;

    let request = new XMLHttpRequest();
    request.open("POST", "http://localhost:8088/auth", true);

    let data = {
        username: name,
        password: password,
    };
    let message = {
        command: SIGN_IN,
        data: JSON.stringify(data),
    };
    request.send(JSON.stringify(message));

}