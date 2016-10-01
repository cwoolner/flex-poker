import React from 'react';
import WebSocketService from '../webSocket/WebSocketService';

export default React.createClass({

  componentWillMount() {
    WebSocketService.disconnect();

    var token = document.querySelector("meta[name='_csrf']").content;
    var header = document.querySelector("meta[name='_csrf_header']").content;

    var myHeaders = new Headers();
    myHeaders.append(header, token);

    var myInit = {
      method: 'POST',
      headers: myHeaders,
      cache: 'no-cache',
      redirect: 'manual',
      credentials: 'same-origin'
    };

    fetch('/logout', myInit).then(response => location.href = response.url);
  },

  render() {
    return null;
  }
})
