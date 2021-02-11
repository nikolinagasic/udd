const getAll = () =>
    fetch("https://localhost:8081/request", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token"))
        }
    })
        .then(response => response.json())
        .catch((error) => {
            console.error("Error", error);
        })

const approve = (requestId) =>
    fetch("https://localhost:8081/request/approve/" + requestId, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token"))
        }
    })
        .then(response => response.json())
        .catch((error) => {
            console.error("Error", error);
        })

const decline = (requestId) =>
    fetch("https://localhost:8081/request/decline/" + requestId, {
        method: "Delete",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + JSON.parse(localStorage.getItem("token"))
        }
    })
        .then(response => response.json())
        .catch((error) => {
            console.error("Error", error);
        })

const service = {
    getAll,
    approve,
    decline
}

export default service;