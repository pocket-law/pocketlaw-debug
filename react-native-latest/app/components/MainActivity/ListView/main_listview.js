import React, {Component} from 'react';
import {AppRegistry, Text, View, ListView, StyleSheet, TouchableOpacity, Linking, Alert, ToastAndroid} from 'react-native';

const jsonString = '';

const jsonObj = null;

const mDictJson = require('./json/dict.json');

// This variable is used to avoid searching again when clicking the hamburger menu after a search
const lastSearch = '';

var DomParser = require('react-native-html-parser').DOMParser

export default class MainListView extends Component{
    constructor(){
        super();
        const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        this.state = {
            termDataSource: ds,
            resultsArray: [],
            searchTerm:  ''

        };
    }


    componentWillReceiveProps(nextProps) {

    }

    componentDidMount(){
        this.getInternalJson();

        let html =
            `<html>
                <body>
                    <div id="b">
                        <a href="example.org">
                        <div class="inA">
                            <br>bbbb</br>
                        </div>
                    </div>
                    <div class="bb">
                        Test
                    </div>
                </body>
            </html>`
        let doc = new DomParser().parseFromString(html,'text/html')

        console.log("doc.querySelect('#b .inA'): \n" + doc.querySelect('#b .inA'))
        console.log("doc.getElementsByTagName('a'): \n" + doc.getElementsByTagName('a'))
        console.log("doc.querySelect('#b a[href=\"example.org\"]'): \n" + doc.querySelect('#b a[href="example.org"]'))

    }

    // TODO: remove this json jazz.
    getInternalJson(){
        this.setState({
            termDataSource: this.state.termDataSource.cloneWithRows(mDictJson.terms, 'term')
        });

        jsonString = JSON.stringify(mDictJson);
    }


    renderRow(term, sectionId, rowId, highlightRow){
        return(
            <View>
                <Text>ROW TEXT</Text>
            </View>
        )
    }

    render(){
        return(
            <ListView
                style={styles.listView}
                ref='mainListviewRef'
                dataSource={this.state.termDataSource}
                renderRow={this.renderRow.bind(this)} />
        );
    }
}


const styles = StyleSheet.create({
    listView: {

    }
});

AppRegistry.registerComponent('MainListView', () => MainListView);
