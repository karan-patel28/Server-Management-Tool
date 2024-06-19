from fastapi import FastAPI
from routers import control

app = FastAPI()

app.include_router(control.router, prefix="/control", tags=["control"])

@app.get("/")
def read_root():
    return {"message": "Welcome to the PC Control Server"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
