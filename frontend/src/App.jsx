import './App.css'
import Register from "./pages/Register.tsx";
import {BrowserRouter, Routes, Route} from "react-router-dom";
import Login from "./pages/Login.tsx";
function App() {
  return (
    <>
     <BrowserRouter>
         <Routes>
             <Route path={"/register"} element={<Register />} />
             <Route path={"/login"} element={<Login/>} />
         </Routes>
     </BrowserRouter>
    </>
  )
}

export default App
