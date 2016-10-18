import React from 'react';
import { connect } from 'react-redux';
import NavigationComponent from './NavigationComponent';

const mapStateToProps = state => ({ username: state.username })

export default connect(mapStateToProps)(NavigationComponent)
