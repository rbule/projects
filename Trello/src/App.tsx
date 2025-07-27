import { useState,useEffect,useRef,createContext} from 'react'
import Column from './Column'
import Header from './Header'
import ChooseApiKey from './ChooseApiKey'
import './App.css'
import Order from './Cart'
import type {cardType,orderType,contextType,inCartType} from './types'



export const MoveCardContext = createContext<contextType | undefined>(undefined);
export const ObjectRefContext = createContext<React.RefObject<HTMLDivElement | null> | undefined>(undefined);
export const InCartContext = createContext<inCartType>({});

function App() {
  const [userId, setUserId] = useState<number | null>(null);
  const [apiKey, setApiKey] = useState<string>('');
  const [cards, setCards] = useState<cardType[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [myOrders, setMyOrders] = useState<orderType[]>([]);
  const [orders, setOrders] = useState<cardType[]>([]);
  const [inCart, setInCart] = useState<inCartType>({});
  const [query, setQuery] = useState<string>('');
  const [pending, setPending] = useState<cardType[]>([]);
  const allProducts = useRef<cardType[]>([]);
  const orderRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    fetch('/api/products')
        .then(res => res.json())
        .then(data => {
        setCards(data);
        setLoading(false);
        allProducts.current = data;
        const initialInCart: inCartType = {};
        data.forEach((card: cardType) => {
            initialInCart[card.id] = false;
        });
        setInCart(initialInCart);
    })
        .catch(err => {
        console.error("Fetch error:", err);
    fetch('api/order/myOrders')
        .then(res => res.json())
        .then(data => {
          setMyOrders(data);
        })
    });
  }, []);

  useEffect(() => {
  if(apiKey === '') return;
  fetch('/api/order/myOrders',{
      method:'GET',
      headers:{
          'Authorization': 'App ' + apiKey,
          "Content-Type": "application/json",
          "Accept": "application/json"
      }
      })
      .then(res => res.json())
      .then(res => {
        setMyOrders(res);
      })
      .catch((err) => console.error(err));
  },[apiKey]);

  useEffect(() => {
    const updateDisplayedCards = (productSource: cardType[]) => {
        setCards(productSource.filter(card => !inCart[card.id]));
    };

    if (query === '') {
        updateDisplayedCards(allProducts.current);
    } else {
        fetch('/api/products/search?query=' + query)
            .then(res => res.json())
            .then(res => updateDisplayedCards(res))
            .catch((err) => console.log("Fetch error:",err));
    }
  },[query, inCart]);

  useEffect(() => {
    if (pending.length === 0) return;

    pending.forEach(order => {
      fetch('/api/order',{
        method:'POST',
        headers:{
            'Authorization': 'App ' + apiKey,
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body:JSON.stringify({
          'id': order.id,
          'amountBought':1,
          'rating':5
        })
      })
      .then(res => res.json())
      .then(res => {
        const orderId = res.id;
        const recievedOrder = res;
        const orderFetch = (orderId:number,recievedOrder:orderType) => {
          fetch('/api/order/status/' + orderId,{
            method:'GET',
            headers:{
              'Authorization': 'App ' + apiKey,
              "Content-Type": "application/json",
              "Accept": "application/json"
            }
          })
          .then(res => res.json())
          .then(res => {
            if(res.status === 'REJECTED'){
              alert('Order with OrderId ' + orderId + ' has been rejected!');
              setPending(p => p.filter(o => o.id !== order.id));
            }
            else if (res.status === 'PENDING') return new Promise(resolve =>
              setTimeout(() => resolve(orderFetch(orderId,recievedOrder)), 1000));
            else{
              setMyOrders(o => [...o,recievedOrder]);
              setPending(p => p.filter(o => o.id !== order.id));
            }
          })
          .catch(err => console.log(err));
        }

        orderFetch(orderId,recievedOrder);
      }).catch(err => console.log(err));
    
    })
  }, [pending]);

  
  const handleLogin = (id: number) => {
    setUserId(id);
  }

  const handleLogout = () => {
    setUserId(null);
    setOrders([]);
    setPending([]);
    const resetInCart: inCartType = {};
    allProducts.current.forEach(card => {
        resetInCart[card.id] = false;
    });
    setInCart(resetInCart);
  }

  function moveCard(card: cardType,where='cart'){
    if(where === 'cart'){
      setCards(prevCards => prevCards.filter(c => c.id !== card.id));
      setOrders(orders => [...orders, card]);
      setInCart(prevInCart => ({ ...prevInCart, [card.id]: true }));
    }
    else if(where === 'products'){
      setOrders(orders => orders.filter(o => o.id != card.id));
      setCards(c => [...c, card]);
      setInCart(prevInCart => ({ ...prevInCart, [card.id]: false }));
    }
    else if(where === 'productsBuy'){
      setPending(orders);
      setOrders([]);
    }
  }


  return (
    <>
    <div className="app-container">
      <Header userId={userId} onLogout={handleLogout} />
    </div>

    {userId !== null && !loading &&(
      <div className="columnsContainer">
        <ObjectRefContext.Provider value={orderRef}>
        <MoveCardContext.Provider value={{moveCard}} >
        <InCartContext.Provider value={inCart}>
          <Column type="products" products={cards} setQuery={setQuery}/>
        <Column type="orders" products={allProducts.current} orders={myOrders}/>
        <Order type="cart" orders={orders} orderRef={orderRef} />
        <Order type="pending" pending={pending} />
        </InCartContext.Provider>
        </MoveCardContext.Provider>
        </ObjectRefContext.Provider>
        
      </div>
    )}
    {(userId === null || loading) && (
      <div className="columnsContainer">
        <ChooseApiKey setApiKey={setApiKey} onLogin={handleLogin} />
        <Column type="loading"/>
        <Column type="loading"/>
        <Order type="cart" orders={orders} orderRef={orderRef} />
        <Order type="pending" pending={pending} />
      </div>
    )}

    </>
  )
}

export default App