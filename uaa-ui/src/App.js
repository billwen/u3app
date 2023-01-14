import logo from './logo.svg';
import './App.css';

function App() {
  return (
    <div className="App">
      <form action="#">
        <label htmlFor="username">用户名</label>
        <input type="text" id="username" name="username"/>

        <label htmlFor="">密码</label>
        <input type="password" id="password" name="password"/>

        <button type="submit">登录</button>

        <h3>还没有注册?</h3>
      </form>


    </div>
  );
}

export default App;
