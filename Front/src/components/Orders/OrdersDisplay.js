import testAxios from "../../apis/TestAxios"
import { useEffect, useState } from "react"
import { Button, Col, FormControl, FormLabel, Row, Table } from 'react-bootstrap';
import { jwtDecode } from 'jwt-decode';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCancel, faListCheck } from "@fortawesome/free-solid-svg-icons";


export const OrderDisplay = () => {
    const [orders, setOrders] = useState([])
    const [searchParams, setSearchParams] = useState({ dateFromParam: "", dateToParam: "" });




    const getOrders = () => {
        const params = {};
        if (searchParams.dateFromParam)
            params.dateFromParam = searchParams.dateFromParam;
        if (searchParams.dateToParam)
            params.dateToParam = searchParams.dateToParam;

        testAxios.get(`/orders`, { params })
            .then((res) => {
                setOrders(res.data);
                console.log(res.data);
            })
            .catch((error) => {
                console.log(error);
            });
    };

    useEffect(() => {
        getOrders();
    }, []);


    const isLoggedIn = !!localStorage.getItem("jwt")

    const extractInfoFromTokenUser = () => {
        const token = localStorage.getItem("jwt")
        const decoded = jwtDecode(token)

        if (decoded.role.authority === "USER") {
            return true
        }
        return false;
    }


    const isUser = isLoggedIn ? extractInfoFromTokenUser() : false;


    const handleSearchChange = (e) => {
        const { name, value } = e.target;
        const formattedValue = name.includes("date") ? value.replace('T', ' ') : value;


        setSearchParams(prevParams => ({
            ...prevParams,
            [name]: formattedValue
        }));
        console.log("Updated Search Params:", { ...searchParams, [name]: formattedValue });
    };

    function datum(dateString) {
        const date = new Date(dateString);

        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();

        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');

        return `${month}.${day}.${year}. ${hours}:${minutes}`;
    }

    const goToCancelOrder = (orderId) => {
        if (window.confirm('Are you sure you want to cancel order?')) {

            testAxios.delete('/orders/' + orderId)
                .then(res => {
                    console.log(res);

                    alert('Order successfully cancelled');
                    window.location.reload();
                })
                .catch(error => {
                    if (error.response && error.response.status === 400) {
                        alert('The order cannot be canceled because it was created more than 12 hours ago!');
                    } else {
                        console.log(error);
                        alert('An error has occurred');
                    }
                });
        }



    }
    return (
        <Col>
            <Row>
                <Col>
                    <FormLabel>Date from:</FormLabel>
                    <FormControl style={{ width: '100%' }} placeholder="Start date" type="datetime-local" name="dateFromParam" onChange={handleSearchChange} />
                </Col>
                <Col>
                    <FormLabel>Date to:</FormLabel>
                    <FormControl style={{ width: '100%' }} placeholder="End date" type="datetime-local" name="dateToParam" onChange={handleSearchChange} />
                </Col>
                <Col>
                    <Button style={{ marginLeft: '150px' }} onClick={getOrders}>Search</Button>
                </Col>
            </Row>

            <Row>
                <Col>
                    <br />
                    <Table className="table table-striped">
                        <thead>
                            <tr>
                                <th >Product</th>
                                <th>Creation date</th>
                                <th>Quantity</th>
                                <th>Total price (€)</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {orders.map((order) => (
                                <tr key={order.id}>
                                    <td >{order.productName}</td>
                                    <td>{datum(order.date)}</td>
                                    <td>{order.quantity}</td>
                                    <td>{order.totalPrice} €</td>
                                    <td>
                                        {isUser ? <Button className="btn btn-danger me-1" onClick={() => goToCancelOrder(order.id)}> <FontAwesomeIcon icon={faCancel} /> Cancel</Button>
                                            : <Button disabled className="btn btn-danger me-1" onClick={() => goToCancelOrder(order.id)}><FontAwesomeIcon icon={faListCheck} />Cancel</Button>}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </Col>
            </Row>
        </Col>)
}