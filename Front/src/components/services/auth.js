import TestAxios from "../../apis/TestAxios"

export const login = async (username, password) => {

    const body = {
        username: username,
        password: password
    }
    try{
        const resp = await TestAxios.post("/users/auth", body)
        window.localStorage.setItem("jwt", resp.data)
        window.location.replace("http://localhost:3000")
    }catch(e){
        console.log(e)
        alert("Incorrect username or password! Try again")
        window.location.replace("http://localhost:3000/login")
        
    }
}

export const logout = () => {
    window.localStorage.removeItem("jwt")
    window.location.replace("http://localhost:3000")
}