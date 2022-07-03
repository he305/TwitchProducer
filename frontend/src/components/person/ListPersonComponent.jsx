import React, { Component } from 'react'
import PersonService from '../../services/PersonService'

class ListPersonComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            persons: []
        }

        this.addPerson = this.addPerson.bind(this);
    }

    componentDidMount() {
        PersonService.getPersons().then((res) => {
            this.setState({persons: res.data.persons});
        });
    }

    addPerson(){
        this.props.history.push('/add-person/_add');
    }

    render() {
        return (
            <div>
                 <h2 className="text-center">Person List</h2>
                 <div className = "row">
                    <button className="btn btn-primary" onClick={this.addPerson}> Add Person</button>
                 </div>
                 <br></br>
                 <div className = "row">
                        <table className = "table table-striped table-bordered">

                            <thead>
                                <tr>
                                    <th> Person First Name</th>
                                    <th> Person Last Name</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.persons.map(
                                        person => 
                                        <tr key = {person.id}>
                                             <td> { person.firstName} </td>   
                                             <td> {person.lastName}</td>
                                        </tr>
                                    )
                                }
                            </tbody>
                        </table>

                 </div>

            </div>
        )
    }
}

export default ListPersonComponent;