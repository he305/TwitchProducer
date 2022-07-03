import React from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import ListPersonComponent from './components/person/ListPersonComponent';
import CreatePersonComponent from './components/person/CreatePersonComponent';


function App() {
    return (
        <div>
            <Router>
                  {/* <HeaderComponent /> */}
                    <div className="container">
                        <Switch> 
                              <Route path = "/" exact component = {ListPersonComponent}></Route>
                              <Route path = "/persons" component = {ListPersonComponent}></Route>
                              <Route path = "/add-person/:id" component = {CreatePersonComponent}></Route>
                              {/*<Route path = "/view-employee/:id" component = {ViewEmployeeComponent}></Route> */}
                              {/* <Route path = "/update-employee/:id" component = {UpdateEmployeeComponent}></Route> */}
                        </Switch>
                    </div>
                  {/* <FooterComponent /> */}
            </Router>
        </div>
        
      );
}

export default App;