import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { Button, Col, FormCheck, FormControl, FormLabel, FormSelect, Modal, Row, Table } from 'react-bootstrap';
import { jwtDecode } from 'jwt-decode';
import testAxios from "../../apis/TestAxios";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faList, faListCheck, faRemove } from "@fortawesome/free-solid-svg-icons";


export const ProductDisplay = () => {
    const [products, setProducts] = useState([])
    const [pageNo, setPageNo] = useState(0)
    const [pageCount, setPageCount] = useState(0)
    const [searchParams, setSearchParams] = useState()
    const [stores, setStores] = useState([])
    const [showSearch, setShowSearch] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [selectedProductId, setSelectedProductId] = useState(null);
    const [quantity, setQuantity] = useState('');

    const navigate = useNavigate();

    const getProducts = () => {
        testAxios.get((`/products?pageNo=${pageNo}`), {
            params: { ...searchParams }


        })
            .then(res => {
                setPageCount(Number(res.headers["total-pages"]))
                setProducts(res.data)
                console.log(res.data)

            })
            .catch(error => {
                console.log(error)
            })
    }

    const getStores = () => {
        testAxios.get("/stores")
            .then(res => {
                console.log(res)
                setStores(res.data)
            })
            .catch(error => {
                console.log(error)
            })
    }

    useEffect(() => {
        getProducts()
        getStores()
    }, [pageNo])

    const isLoggedIn = !!localStorage.getItem("jwt")

    const extractInfoFromTokenAdmin = () => {
        const token = localStorage.getItem("jwt")
        const decoded = jwtDecode(token)
        console.log(decoded.role.authority)

        if (decoded.role.authority === "ADMIN") {
            return true
        }
        return false;
    }


    const extractInfoFromTokenUser = () => {
        const token = localStorage.getItem("jwt")
        const decoded = jwtDecode(token)
        console.log(decoded.role.authority)

        if (decoded.role.authority === "USER") {
            return true
        }
        return false;
    }


    const isUser = isLoggedIn ? extractInfoFromTokenUser() : false;
    const isAdmin = isLoggedIn ? extractInfoFromTokenAdmin() : false;

    const goToAddProduct = () => {
        navigate("/products/adding");
    };


    const goToOrdered = (productId) => {
        setSelectedProductId(productId);
        setShowModal(true);
    };

    const handleModalClose = () => {
        setShowModal(false);
        setQuantity('');
    };

    const handleModalSave = () => {
        const dto = {
            productId: selectedProductId,
            quantity: parseInt(quantity, 10)
        };

        testAxios.post('/orders/', dto)
            .then(res => {
                console.log(res);
                handleModalClose()
                alert('Order added successfully');
                window.location.reload();

            })
            .catch(error => {

                console.log(error);
                alert('You cannot order that quantity of products.');

            });


    };

    const goToDelete = (productId) => {
        if (window.confirm('Are you sure you want to delete the product?')) {

            testAxios.delete('/products/' + productId)
                .then(res => {
                    console.log(res);

                    alert('Product removed successfully');
                    window.location.reload();
                })
                .catch(error => {
                    if (error.response && error.response.status === 400) {
                        alert('The product cannot be deleted because it has related orders.');
                    } else {
                        console.log(error);
                        alert('An error has occurred');
                    }
                });
        }
    }

    const renderStores = () => {
        return stores.map(store => (
            <option key={store.id} value={store.id}>{store.name}</option>
        ));
    };

    const handleSearchChange = (e) => {

        const { name, value } = e.target;

        setSearchParams(prevParams => ({
            ...prevParams,
            [name]: value
        }));
        console.log("Updated Search Params:", { ...searchParams, [name]: value });
        setPageNo(0)
    };
    return (
        <Col>


            {isAdmin && <Button className="addButton" onClick={goToAddProduct}>Add product</Button>}
            <div className="checkbox-label">
                <FormCheck type="checkbox" checked={showSearch} onChange={() => setShowSearch(!showSearch)} />
                <FormLabel className="form-check-label">Show/hide search</FormLabel>
            </div>
            {showSearch && (
                <div>
                    <Row className="justify-content-center">
                        <Col md={9}>
                            <div style={{ textAlign: 'left', fontStyle: "oblique" }}>
                                <FormLabel>Store:</FormLabel>
                            </div>
                            <FormSelect style={{ textAlign: 'center', width: '900px', borderColor: "grey", borderStyle: "groove" }} name="storeId" onChange={handleSearchChange}>
                                <option value={""}>Choose a store</option>
                                {renderStores()}
                            </FormSelect>
                        </Col>
                    </Row>
                    <Row className="justify-content-center">
                        <Col md={9}>
                            <div style={{ textAlign: 'left', fontStyle: "oblique" }}>
                                <FormLabel>Name of product:</FormLabel>
                            </div>
                            <FormControl style={{ textAlign: 'center', width: '900px', borderColor: "grey", borderStyle: "groove" }} placeholder="Name of product" type="text" name="name" onChange={handleSearchChange} />
                        </Col>
                    </Row>
                    <Row className="justify-content-center">
                        <Col md={9}>
                            <div style={{ textAlign: 'left', fontStyle: "oblique" }}>
                                <FormLabel>Price from:</FormLabel>
                            </div>
                            <FormControl style={{ textAlign: 'center', width: '900px', borderColor: "grey", borderStyle: "groove" }} placeholder="Price from" type="number" name="priceFrom" onChange={handleSearchChange} />
                        </Col>
                    </Row>
                    <Row className="justify-content-center">
                        <Col md={9}>
                            <div style={{ textAlign: 'left', fontStyle: "oblique" }}>
                                <FormLabel>Price to:</FormLabel>
                            </div>
                            <FormControl style={{ textAlign: 'center', width: '900px', borderColor: "grey", borderStyle: "groove" }} placeholder="Price to" type="number" name="priceTo" onChange={handleSearchChange} />
                        </Col>
                    </Row>

                    <Row>
                        <Col>
                            <Button style={{ marginLeft: '90%', marginTop: "2%", borderStyle: "dashed" }} onClick={getProducts}>Search</Button>
                        </Col>
                    </Row>
                </div>
            )}
            <br />
            <Row>
                <Col>
                    <Table className="table table-hover">
                        <thead>
                            <tr>
                                <th >Name</th>
                                <th> Price (€)</th>
                                <th>Available quantity</th>
                                <th>Store </th>
                                {isAdmin ? <th>Action</th> : ""}

                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {products.map((product) => (
                                <tr key={product.id}>
                                    <td >{product.name}</td>
                                    <td >{product.price} €</td>
                                    <td>{product.available < 1 ? (<span style={{ color: 'red' }}>Unavailable</span>) : (product.available)}  </td>
                                    <td>{product.storeName}</td>
                                    <td>
                                        {isAdmin && <Button id="btn" className="btn-danger me-1" onClick={() => goToDelete(product.id)}><FontAwesomeIcon icon={faRemove} />Delete</Button>}
                                        {isUser && product.available > 0 ? <Button className="btn btn-primary me-1" onClick={() => goToOrdered(product.id)}> <FontAwesomeIcon icon={faList} /> Order</Button>
                                            : <Button disabled className="btn btn-primary me-1" onClick={() => goToOrdered()}><FontAwesomeIcon icon={faListCheck} />Order</Button>}

                                    </td>
                                    <td></td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                    {products.length !== 0 ? (<div className="page">
                        <Button onClick={() => { setPageNo(pageNo - 1) }} disabled={pageNo === 0}> Previous</Button>
                        <>   </>
                        <Button onClick={() => { setPageNo(pageNo + 1) }} disabled={pageNo === pageCount - 1}>Next</Button>
                    </div>) : undefined
                    }
                </Col>
            </Row>
            <Modal show={showModal} onHide={handleModalClose}>
                <Modal.Header closeButton>
                    <Modal.Title style={{ color: 'black', fontSize: '20px', fontWeight: 'bold', textAlign: "center" }}>Enter the quantity</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <FormControl
                        type="number"
                        placeholder="Quantity"
                        value={quantity}
                        onChange={(e) => setQuantity(e.target.value)}
                    />
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="dark" onClick={handleModalClose}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleModalSave}>
                        Save
                    </Button>
                </Modal.Footer>
            </Modal>

        </Col>
    )
}