import React from 'react';
import { FormGroup, FormControl, FieldGroup, Button } from 'react-bootstrap';

class Chat extends React.Component {

  constructor(props) {
    super(props);
    this.chatFormSubmitted = this.chatFormSubmitted.bind(this);
  }

  displayChat(message) {
    const chatTextArea = document.querySelector('#chat-text');
    const scrollHeight = chatTextArea['scrollHeight'];
    chatTextArea['scrollTop'] = scrollHeight;
    chatTextArea.value += message + '\n';
  }

  chatFormSubmitted(evt) {
    evt.preventDefault();
    const messageTextBox = evt.target.elements[0];
    if (messageTextBox.value) {
      this.props.sendChat(messageTextBox.value);
      messageTextBox.value = '';
    }
  }

  render() {
    return (
      <div className={'chat-area'}>
        <FormGroup>
          <FormControl componentClass="textarea" id="chat-text" disabled="disabled" />
        </FormGroup>
        <form onSubmit={this.chatFormSubmitted}>
          <FormGroup>
            <FormControl type="text" placeholder="Chat..." />
          </FormGroup>
        </form>
      </div>
    )
  }

}

export default Chat
