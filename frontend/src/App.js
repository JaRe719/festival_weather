import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Layout from './Pages/Layout/Layout';
import Home from './Pages/Home/Home';
import Adding from './Pages/Adding/Adding';
import All from './Pages/All/All';
import Detail from './Pages/Detail/Detail';
import NoPage from './Pages/NoPage/NoPage';
import NotFound from './Pages/NotFound/NotFound';


const App = () => {
  return (
    <div>
      <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path='/add' element={<Adding />}/>
          <Route path='/all' element={<All />} />
          <Route path='/detail' element={<Detail />} />
          <Route path='/notfound' element={<NotFound />} />
          <Route path='*' element={<NoPage />} />
        </Route>
        
      </Routes>
      </BrowserRouter>
      
    </div>
  )
}

export default App
