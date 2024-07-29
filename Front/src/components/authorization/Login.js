import { useState } from "react"
import { Row, Col, Form, Button, InputGroup } from "react-bootstrap"
import { login } from "../services/auth"
import { useNavigate } from "react-router-dom"

const Login = () => {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [showPassword, setShowPassword] = useState(false)

    const navigate = useNavigate()
    

    const goToRegistration = () => {
        navigate("/registration")
    }

    return (
        <div className="loginForm">
            <div className="login-box">
                <Row className="justify-content-center">
                    <Col>
                        <Form>
                            <Form.Group>
                                <Form.Label className="form">Username</Form.Label>
                                <Form.Control
                                    className="form-label-login"
                                    type="text"
                                    onChange={(e) => setUsername(e.target.value)}
                                />
                            </Form.Group>
                            <Form.Group>
                                <Form.Label className="form">Password</Form.Label>
                                <InputGroup className="input-group mb-3">
                                    <Form.Control
                                        className="form-label-login"
                                        type={showPassword ? "text" : "password"}
                                        onChange={(e) => setPassword(e.target.value)}
                                    />
                                    <Button
                                        variant="outline-primary"
                                        className="show-password-button"
                                        onClick={() => setShowPassword(!showPassword)}
                                    >
                                        {showPassword ? "Hide" : "Show"}
                                    </Button>
                                </InputGroup>
                            </Form.Group>
                            <div className="login-button-group">
                                <Button className="login-button" onClick={() => login(username, password)}>Login</Button>
                                <Button className="login-button" onClick={goToRegistration}>Registration</Button>
                            </div>
                        </Form>
                    </Col>
                </Row>
            </div>
        </div>
    )
}

export default Login
