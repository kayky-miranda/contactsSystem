async function fazerLogin() {
  const login = document.getElementById("login").value;
  const senha = document.getElementById("senha").value;

  try {
    const response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        login: login,
        password: senha // ✅ CORRETO
      })
    });

    const data = await response.json();

    localStorage.setItem("token", data.token);

    document.getElementById("resultado").innerText = "Login OK!";
  } catch (error) {
    document.getElementById("resultado").innerText = "Erro no login";
    console.error(error);
  }
}