import React from 'react';

export default React.createClass({

  displayChat(message) {
    const chatTextArea = document.querySelector('#chat-text');
    const scrollHeight = chatTextArea['scrollHeight'];
    chatTextArea['scrollTop'] = scrollHeight;
    chatTextArea.value += message + '\n';
  },

  chatFormSubmitted(evt) {
    evt.preventDefault();
    const messageTextBox = evt.target.elements[0];
    if (messageTextBox.value) {
      this.props.sendChat(messageTextBox.value);
      messageTextBox.value = '';
    }
  },

  render() {
    return (
      <div className={'chat-area'}>
        <textarea id="chat-text" disabled="disabled"></textarea>
        <form onSubmit={this.chatFormSubmitted}>
          <div>
            <label>Message:</label>
            <input type="text" />
            <input type="submit" value="Send" />
          </div>
        </form>
      </div>
    )
  }
})
