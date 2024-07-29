import React from 'react'
import { createRoot } from 'react-dom/client'
import { Link, Navigate, Route, BrowserRouter as Router, Routes } from 'react-router-dom'
import Home from './components/Home'
import { Button, Container, Nav, Navbar } from 'react-bootstrap'
import Login from './components/authorization/Login'
import { logout } from './components/services/auth'
import Logo from './components/Logo'
import { Registration } from './components/authorization/Registration'
import { ProductDisplay } from './components/Products/ProductDisplay'
import { Adding } from './components/Products/Adding'
import { OrderDisplay } from './components/Orders/OrdersDisplay'



const App = function () {
    if (window.localStorage["jwt"]) {
        return (
            <div>
                <Router>
                    <Navbar collapseOnSelect expand="sm" bg="primary" variant="dark">
                        <Navbar.Brand as={Link} to="/">
                            <Logo />
                            Home
                        </Navbar.Brand>
                        <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                        <Navbar.Collapse id="responsive-navbar-nav">
                            <Nav className="me-auto">
                                <Nav.Link as={Link} to="products"> Products </Nav.Link>
                                <Nav.Link as={Link} to="orders"> Orders </Nav.Link>

                            </Nav>
                            <Nav className="ms-auto">
                                <div style={{ marginRight: "20px" }}>
                                    <Button id='logout' onClick={logout}>Logout</Button>
                                </div>
                            </Nav>
                        </Navbar.Collapse>
                    </Navbar>
                    <Container style={{ paddingTop: "10px", textAlign: "center" }}>
                        <Routes>
                            <Route path='/' element={<Home />} />
                            <Route path="/products" element={<ProductDisplay />} />
                            <Route path="/products/adding" element={<Adding />} />
                            <Route path="/orders" element={<OrderDisplay />} />


                        </Routes>
                    </Container>
                </Router>
            </div>
        )
    } else {
        return (
            <Router>
                <Navbar collapseOnSelect expand="sm" bg="primary" variant="dark">
                    <Navbar.Brand as={Link} to="/">
                        <Logo />
                        Home
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                    <Navbar.Collapse id="responsive-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link as={Link} to="products"> Products </Nav.Link>
                        </Nav>
                        <Nav className="ms-auto">

                            <div className="d-flex align-items-center">
                                <Nav.Link style={{ color: "white" }} as={Link} to="/login"> Login </Nav.Link>
                                <Nav.Link style={{ color: "white" }} as={Link} to="/registration"> Registration </Nav.Link>

                            </div>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                <Container style={{ paddingTop: "10px", textAlign: "left" }}>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/products" element={<ProductDisplay />} />
                        <Route path="/registration" element={<Registration />} />

                        <Route path="*" element={<Navigate replace to="/login" />} />
                    </Routes>
                </Container>
            </Router>

        );
    }
}

const rootElement = document.getElementById('root')
const root = createRoot(rootElement)

root.render(
    <App />
)