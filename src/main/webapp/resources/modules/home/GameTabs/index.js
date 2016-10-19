import React from 'react';
import { connect } from 'react-redux';
import GameTabList from './GameTabList';

const mapStateToProps = state => ({ openGameTabs: state.openGameTabs })

export default connect(mapStateToProps)(GameTabList)
