import type {HeaderProps} from './types'

export default function Header({ userId, onLogout }: HeaderProps) {
  return (
    <div className="headerClass">
      {userId !== null ? (
        <>
          <h1>Welcome User {userId}</h1>
          <button onClick={onLogout}>Logout</button>
        </>
      ) : (
        <h1>Please Log In</h1>
      )}
    </div>
  );
}