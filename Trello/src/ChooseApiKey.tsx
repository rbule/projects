import { useState, useEffect } from "react";

interface ChooseApiKeyProps {
  setApiKey: (apiKey: string) => void,
  onLogin: (userId: number) => void
}

export default function ChooseApiKey({ setApiKey, onLogin }: ChooseApiKeyProps) {
    const [selectedUserId, setSelectedUserId] = useState<number>(0);
    const userApiKeys: { [key: number]: string } = {
        1: "9e26e2c2-4c3a-4a9b-b2b1-0b3f3f2faed1",
        2: "e1f6c7a0-2333-4b77-90dc-20e4e3d1a8b0",
        3: "b9a4f32f-1db2-4420-a0b3-c3a0d9a93f0a",
        4: "b5c9d2a7-6ea9-4c5b-90d5-cc8e0cf9c4b2",
        5: "a8f3e8f1-439d-4bd3-9f5d-71561a8f1aa7",
        6: "01dd37c2-5c42-4a84-b0f6-60a1b32478ee",
        7: "d733c01d-feb5-4c2d-9967-23b88cd709af",
        9: "f27ea65f-8fc6-4f3e-b7f1-8c1e0a934cff",
        10: "9d831aa5-8e4e-47c4-988e-6abf7a11a023",
    };

    useEffect(() => {
        if(selectedUserId === 0) return;
        const key = userApiKeys[selectedUserId];
        if (key) setApiKey(key);
        }, [selectedUserId, setApiKey]);

        const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedUserId(Number(e.target.value));
    };

    const handleLogin = () => {
        onLogin(selectedUserId);
    };

    return (
    <div className="loginOverlay">
        <div className="loginPopup">
        <select
            name="api-key-select"
            id="api-key-select"
            value={selectedUserId}
            onChange={handleChange}
        >
            <option value={1}>User 1</option>
            <option value={2}>User 2</option>
            <option value={3}>User 3</option>
            <option value={4}>User 4</option>
            <option value={5}>User 5</option>
            <option value={6}>User 6</option>
            <option value={7}>User 7</option>
            <option value={9}>User 9</option>
            <option value={10}>User 10</option>
        </select>
        <button onClick={handleLogin}>Login</button>
        </div>
    </div>
    );
}