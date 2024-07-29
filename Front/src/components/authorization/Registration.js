import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { Button, Col, Row, Form } from 'react-bootstrap';
import testAxios from "../../apis/TestAxios";


export const Registration = ()=>{

    const [user, setUser] = useState({
        usernameInit: "",
        eMailInit: "",
        nameInit: "",
        surnameInit: "",
        passwordInit: "",
        repeatedPasswordInit: "",  
    })

    const [error, setError] = useState({});   

    const navigate = useNavigate()


    const inputValue = (e) => {
        let input = e.target
       

        let name = input.name
        let value = input.value
        
        setError({ ...error, [name]: "" });         

        setUser({
            ...user,
            [name]: value
            
        });

    }

    const post = () => {

        let newError = {};

        if ( !user.usernameInit) {
            newError.usernameInit = "Enter username!";
            
        }

        if ( !user.eMailInit) {
            newError.eMailInit = "Enter eMail!";
            
        }
        if ( !user.nameInit) {
            newError.nameInit = "Entrer name!";
            
        }

        if ( !user.surnameInit) {
            newError.surnameInit = "Enter surname!";
            
        }
        if ( !user.passwordInit) {
            newError.passwordInit = "Enter password!";
            
        }
        if ( !user.repeatedPasswordInit) {
            newError.repeatedPasswordInit = "Enter a repeated password";
            
        }

        if (Object.keys(newError).length > 0) {
            setError(newError);
            return;
        }

        const dto = {
            username:user.usernameInit,
            eMail: user.eMailInit,
            name: user.nameInit,
            surname: user.surnameInit,
            password: user.passwordInit,
            repeatPassword: user.repeatedPasswordInit

        }
        console.log(dto)
        testAxios.post('/users', dto)
            .then(res => {
                console.log(res)
                alert("Successful registration")
                navigate("/products")
            })
            .catch(error => {
                console.log(error)
                navigate("/registration")

            })
    }
    return(
        <Row className="justify-content-center">
        <Col md={4}>
            <h2>Registration</h2>
            <Form>
                <Form.Label htmlFor="usernameInit">Username</Form.Label>
                <Form.Control type="text" name="usernameInit" id="usernameInit" onChange={(e) => inputValue(e)} />
                {error.usernameInit && <span style={{ color: "red" }}>{error.usernameInit}</span>}

                <br />
                <Form.Label htmlFor="eMailInit">E mail</Form.Label>
                <Form.Control type="text" name="eMailInit" id="eMailInit" onChange={(e) => inputValue(e)} />
                {error.eMailInit && <span style={{ color: "red" }}>{error.eMailInit}</span>}
                <br />
                <Form.Label htmlFor="nameInit"> Name</Form.Label>
                <Form.Control type="text" name="nameInit" id="nameInit" onChange={(e) => inputValue(e)} />
                {error.nameInit && <span style={{ color: "red" }}>{error.nameInit}</span>}
                <br />
                <Form.Label htmlFor="surnameInit"> Surname</Form.Label>
                <Form.Control type="text" name="surnameInit" id="surnameInit" onChange={(e) => inputValue(e)} />
                {error.surnameInit && <span style={{ color: "red" }}>{error.surnameInit}</span>} <br />
                <br/>
                <Form.Label htmlFor="passwordInit"> Password</Form.Label>
                <Form.Control type="password" name="passwordInit" id="passwordInit" onChange={(e) => inputValue(e)} />
                {error.passwordInit && <span style={{ color: "red" }}>{error.passwordInit}</span>}
                <br/>
                <Form.Label htmlFor="repeatedPasswordInit">Repeat password</Form.Label>
                <Form.Control type="password" name="repeatedPasswordInit" id="repeatedPasswordInit" onChange={(e) => inputValue(e)} />
                {error.repeatedPasswordInit && <span style={{ color: "red" }}>{error.repeatedPasswordInit}</span>}
                <br />
               
                <br />
                <Button className="registration-button" onClick={() => post()}>Registration</Button>

            </Form>
        </Col></Row>    )
}