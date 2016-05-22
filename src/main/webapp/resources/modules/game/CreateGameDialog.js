import React from 'react';
import webSocketService from '../common/webSocketService';

export default React.createClass({

  hideDialog() {
    this.props.hideDialog();
  },

  potentiallyHideDialog(evt) {
    if (evt.keyCode === 27) {
      this.hideDialog();
    }
  },

  preventNonNumeric(evt) {
    if (!(evt.which >= 48 && evt.which <= 57)) {
      evt.preventDefault();
    }
  },

  createGameFormSubmitted(evt) {
    evt.preventDefault();
    const nameElement = evt.target.elements[0];
    const playersElement = evt.target.elements[1];
    const playersPerTableElement = evt.target.elements[2];
    const numberOfMinutesBetweenBlindLevelsElement = evt.target.elements[3];

    webSocketService.send('/app/creategame', {
      name: nameElement.value,
      players: playersElement.value,
      playersPerTable: playersPerTableElement.value,
      numberOfMinutesBetweenBlindLevels: numberOfMinutesBetweenBlindLevelsElement.value
    });

    this.hideDialog();
  },

  render() {
    return (
      <div {...this.props}>
        <div className="overlay" onClick={this.hideDialog}></div>
        <div className="content">
          <div className="close" onClick={this.hideDialog}>X</div>
          <form id="create-game-form" onKeyDown={this.potentiallyHideDialog} onSubmit={this.createGameFormSubmitted}>
            <FormField label="Name:" name="name" />
            <FormField label="Players:" name="players" onKeyPress={this.preventNonNumeric} />
            <FormField label="Players per table:" name="playersPerTable" onKeyPress={this.preventNonNumeric} />
            <FormField label="Blind increment (minutes):" name="numberOfMinutesBetweenBlindLevels" onKeyPress={this.preventNonNumeric} />
            <input type="submit" value="Create" />
          </form>
        </div>
      </div>
    )
  }
})

const FormField = (props) => {
  return (
    <div>
      <label>{props.label}</label> <input name={props.name} onKeyPress={props.onKeyPress} />
      <ErrorSpan />
    </div>
  )
}

const ErrorSpan = (props) => <span className="error" />;
