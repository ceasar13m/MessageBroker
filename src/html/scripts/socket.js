const webSocket = new WebSocket('ws://localhost:8080');
let TOKEN;

const SIGN_UP = 0;
const SIGN_IN = 1;
const PUBLISH = 2;
const SUBSCRIBE = 3;
const DISCONNECT = 4;
const CREATE_CHANNEL = 5;


webSocket.onopen = event => {
// alert('open');
};

webSocket.onmessage = event => {
    alert(event.data);
    let jsonSTR = event.data;
    let mess = JSON.parse(jsonSTR);
    if (typeof mess.token === 'undefined')
        TOKEN = null;
    else
        TOKEN = mess.token;
};

webSocket.onclose = event => {
    alert('onclose');
};