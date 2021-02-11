const getAll = () =>
    fetch("https://localhost:8081/api/auth/payment-method", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
        .then(response => response.json())
        .catch((error) => {
            console.error('Error:', error);
        });

const getAllWithoutFirstThree = () =>
    fetch("https://localhost:8081/api/auth/payment-method/other", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
        .then(response => response.json())
        .catch((error) => {
            console.error('Error:', error);
        });

const deletePaymentMethod = (paymentMethodId) =>
    fetch("https://localhost:8081/payment-method/" + paymentMethodId, {
        method: "Delete",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        }
    })
        .then(response => response.json())
        .catch((error) => {
            console.error("Error", error);
        })

const createPaymentMethod = (name) =>
    fetch("https://localhost:8081/payment-method", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify({name})
    })
        .then(response => response.json())
        .catch((error) => {
            console.error("Error", error);
        })


const service = {
    getAll,
    getAllWithoutFirstThree,
    deletePaymentMethod,
    createPaymentMethod
}
export default service;