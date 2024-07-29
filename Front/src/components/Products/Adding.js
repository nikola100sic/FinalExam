import { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { Button, Col, Row, Form, FormCheck } from 'react-bootstrap';
import testAxios from "../../apis/TestAxios";


export const Adding = ()=>{
    let productInit = {
        nameInit: "",
        priceInit: "",
        availableInit: "",
        storeInit: "",
    }

    const [product, setProduct] = useState(productInit)
    const [stores, setStores] = useState([])
    const navigate = useNavigate()

    const [error, setError] = useState({});  



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

    
    const inputValue = (e) => {
        let input = e.target
        let name = input.name
        let value = input.value
        
        setError({ ...error, [name]: "" });           
        console.log(value)

        setProduct({
            ...product,
            [name]: value
            
        });
        
    };

    useEffect(() => {
        getStores()
    }, [])

    const storeSelectionChanged = (e) => {
        if (!e.target.value) return;                        

        let storeId = e.target.value;

        let productFromState = product;
        productFromState.storeInit = storeId;

        setProduct(productFromState);
        console.log(productFromState)

    }

    const post = () => {

        let newError = {};

        if (product.availableInit<0 || product.availableInit>500|| !product.availableInit) {
            newError.availableInit = "The quantity of the article can be in the range from 0 to 500!";
            
        }

        if (!product.nameInit) {
            newError.nameInit = "You must enter a name";
        }

        if (Object.keys(newError).length > 0) {
            setError(newError);
            return;
        }

        const dto = {
            name:product.nameInit,
            price: product.priceInit,
            available: product.availableInit,
            storeId: product.storeInit
        }
        console.log(dto)
        testAxios.post('/products', dto)
            .then(res => {
                console.log(res)
                alert("Successfully added")
                navigate("/products")
            })
            .catch(error => {
                console.log(error)
            })
    }


    return (
        <Row className="justify-content-center">
        <Col md={4}>
            <h2>Adding product</h2>
            <Form>
                <Form.Label htmlFor="nameInit">Name</Form.Label>
                <Form.Control type="text" name="nameInit" id="nameInit" onChange={(e) => inputValue(e)} />
                {error.nameInit && <span style={{ color: "red" }}>{error.nameInit}</span>}

                <br />
                <Form.Label htmlFor="priceInit">Product price</Form.Label>
                <Form.Control type="number" name="priceInit" id="priceInit" onChange={(e) => inputValue(e)} />

                <br />
                <Form.Label htmlFor="availableInit">Available quantity</Form.Label>
                <Form.Control type="number" name="availableInit" id="availableInit" onChange={(e) => inputValue(e)} />
                {error.availableInit && <span style={{ color: "red" }}>{error.availableInit}</span>}

                <br />
                <Form.Group controlId="storeInit">
                        <Form.Label>Store </Form.Label>
                        <Form.Select name='storeInit' onChange={event => storeSelectionChanged(event)}>
                            <option></option>
                            {
                                stores.map((store) => (
                                    <option key={store.id} value={store.id}>{store.name}</option>
                                ))
                            }
                        </Form.Select>
                    </Form.Group>
                <br />
                <Button onClick={() => post()}>Add</Button>

            </Form>
        </Col></Row>    )
}