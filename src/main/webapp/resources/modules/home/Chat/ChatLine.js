import React from 'react';

const displayChat = chat => {
  return chat.systemMessage
    ? `System: ${chat.message}`
    : `${chat.senderUsername}: ${chat.message}`
}

export default ({chat}) => <div>{displayChat(chat)}</div>
