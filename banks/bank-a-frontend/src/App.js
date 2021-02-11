import {Button, Form} from "react-bootstrap";
import './App.css';
import {useState, useEffect} from 'react';

function  App() {

  const [payment, setPayment] = useState("");
  const [pan, setPan] = useState("");
  const [securityCode, setSecurityCode] = useState("");
  const [cardholderName, setCardholderName] = useState("");
  const [expireMonth, setExpireMonth] = useState("");
  const [expireYear, setExpireYear] = useState("");
  const [error, setError] = useState(false);

  useEffect(() => {
    const url = (window.location.href).split("/");
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      },
    };
    fetch("https://localhost:8084/transaction/payment/" + url[3], options)
    .then(response => response.json())
    .then(data => setPayment(data));

  }, [])

  const handleSubmit = () => {
    let formData = {
      pan: pan,
      securityCode: securityCode,
      cardholderName: cardholderName,
      expireDate: expireMonth + "/" + expireYear
    }
    const url = 'https://localhost:8084/transaction/' + payment.id;
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    };
    fetch(url, options)
    .then(response => response.json())
    .then(data => {
      if (data.url != null) {
        console.log(JSON.stringify(data))
        window.location.replace(data.url);
      }
    })
    .catch(error => console.log(error));
  }

  const panOnChangeHandler = (value) => {  
    setPan(value);  
  }

  const securityCodeOnChangeHandler = (value) => {
    setSecurityCode(value);
  }

  const cardholderOnChangeHandler = (value) => {
    setCardholderName(value);
  }

  const expireMonthOnChangeHandler = (value) => {
    setExpireMonth(value);
  }

  const expireYearOnChangeHandler = (value) => {
    setExpireYear(value);
  }

  return (
    <div>
      <div className="App-header">
        <div className="w-25">
          <h1>Bank A</h1>
          { payment &&
            <>
              <h4 className="text-warning">Merchant : {payment.merchant.companyName}</h4>
              <h4 className="text-warning mb-5">Amount : {payment.amount} $</h4>
            </>
          }
          <h5>Enter your card information here</h5>
        </div>
        <Form className="p-3 w-25">
          <Form.Group className="row mb-3 p-2">
            <Form.Label className="w-50" style={{marginTop:"5px"}}>PAN:</Form.Label>
            <Form.Control type="password" className="w-50" autoComplete="off" maxLength="19"
            placeholder="Enter PAN" onChange={(e) => panOnChangeHandler(e.target.value)} />
          </Form.Group>
          <Form.Group className="row mb-3 p-2">
            <Form.Label className="w-50" style={{marginTop:"5px"}}>Security code:</Form.Label>
            <Form.Control type="password" className="w-50"
            placeholder="Enter security code" onChange={(e) => securityCodeOnChangeHandler(e.target.value)} />
          </Form.Group>
          <Form.Group className="row mb-3 p-2">
            <Form.Label className="w-50" style={{marginTop:"5px"}}>Cardholder name:</Form.Label>
            <Form.Control className="w-50" type="text"
            placeholder="Enter cardholder name" onChange={(e) => cardholderOnChangeHandler(e.target.value)} />
          </Form.Group>
          <Form.Group className="row mb-3 p-2">
            <Form.Label className="w-50" style={{marginTop:"5px"}}>Expire date:</Form.Label>
            <div className="row float-right"  style={{width:"50%"}} >
              <select className="form-select" style={{width:"60px"}} onChange={(e) => expireMonthOnChangeHandler(e.target.value)}>
                <option value="">--</option>
                <option value="01">01</option>
                <option value="02">02</option>
                <option value="03">03</option>
                <option value="04">04</option>
                <option value="05">05</option>
                <option value="06">06</option>
                <option value="07">07</option>
                <option value="08">08</option>
                <option value="09">09</option>
                <option value="10">10</option>
                <option value="11">11</option>
                <option value="12">12</option>
              </select>
              <select className="form-select" style={{width:"80px", marginLeft:"10px"}} onChange={(e) => expireYearOnChangeHandler(e.target.value)}>
                <option value="">----</option>
                <option value="20">2020</option>
                <option value="21">2021</option>
                <option value="22">2022</option>
                <option value="23">2023</option>
                <option value="24">2024</option>
                <option value="25">2025</option>
                <option value="26">2026</option>
                <option value="27">2027</option>
                <option value="28">2028</option>
                <option value="29">2029</option>
                <option value="30">2030</option>
                <option value="31">2031</option>
                <option value="32">2032</option>
                <option value="33">2033</option>
                <option value="34">2034</option>
                <option value="35">2035</option>
                <option value="36">2036</option>
                <option value="37">2037</option>
                <option value="38">2038</option>
                <option value="39">2039</option>
              </select>
            </div>
          </Form.Group>
          <div className="row" style={{marginLeft:"-25px", marginRight:"-25px"}}>
            <div className="col-6">
              <Button variant="success" onClick={() => handleSubmit()} className="btn btn-primary button w-100">Pay</Button>
            </div>
            <div className="col-6">
              <Button className="button  w-100">Cancel</Button>
            </div>
          </div>
        </Form>
      </div>
    </div>
  );
  
}

export default App;
