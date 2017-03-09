import React, {Component} from 'react';
import {AppRegistry, Text, View, Navigator} from 'react-native';

import Detail from './app/components/Detail/detail';
import ListPage from './app/components/ListPage/list_page';

export default class PocketLawReactNative extends Component{
  renderScene(route, navigator){
    switch(route.id){
      case 'list_page':
        return (<ListPage navigator={navigator} title="list_page" />)
      case 'detail':
        return (<Detail user={route.user} navigator={navigator} title="detail" />)
    }
  }

  render(){
    return(
      <Navigator
        initialRoute={{id: 'list_page'}}
        renderScene={this.renderScene}
        configureScreen={(route, routeStack) => Navigator.SceneConfigs.FloatFromBottom}
      />
    );
  }
}

AppRegistry.registerComponent('PocketLawReactNative', () => PocketLawReactNative);
