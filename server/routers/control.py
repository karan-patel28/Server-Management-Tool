from fastapi import APIRouter
import os
import pexpect
from dotenv import env

router = APIRouter()

@router.post("/shutdown")
async def shutdown_system():
    if os.name == "nt":  # For Windows
        os.system("shutdown /s /t 1")
    else:  # For Linux (Ubuntu)
        os.system("sudo shutdown now")
    return {"message": "System shutdown initiated"}

@router.post("/restart")
async def restart_system():
    if os.name == "nt":  # For Windows
        os.system("shutdown /r /t 1")
    else:  # For Linux (Ubuntu)
        # os.system("sudo reboot")
        password = env.PASSWORD
        child = pexpect.spawn('sudo reboot')
        child.expect('[sudo] password for karan:')
        child.sendline(password)  # Replace 'yourpassword' with your actual password
        child.expect(pexpect.EOF)
    return {"message": "System restart initiated"}
