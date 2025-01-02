import './App.css'
import Register from "./pages/Register.tsx";
import {BrowserRouter, Routes, Route} from "react-router-dom";
import Login from "./pages/Login.tsx";
import About from "./pages/About.tsx";
function App() {
  return (
    <>
     <BrowserRouter>
         <Routes>
             <Route path={"/register"} element={<Register />} />
             <Route path={"/login"} element={<Login/>} />
             <Route path={"/about"} element={<About/>} />
         </Routes>
     </BrowserRouter>
    </>
  )
}

export default App
