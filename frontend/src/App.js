import { useState } from "react";
import "./App.css";

function App() {
  const [text, setText] = useState("");

  const handleSubmit = async () => {
    try {
      const fullURL = "http://localhost:8080/code-generator?message=" + text;
      const response = await fetch(fullURL, {
        method: "GET",
        mode: "cors",
      });
      console.log(response)
      const data = await response.text();
      console.log(data)
      //alert(`Response: ${data}`);
    } catch (error) {
      console.error(error);
      alert("Error submitting data");
    }
  };

  return (
    <div className="container">
      <div className="rectangle">
        <input
          type="text"
          value={text}
          onChange={(e) => setText(e.target.value)}
          placeholder="Write a sentence..."
          className="text-input"
        />
        <button onClick={handleSubmit} className="submit-button">Submit</button>
      </div>
    </div>
  );
}

export default App;