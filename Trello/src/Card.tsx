import { useEffect, useState, useRef,useContext } from "react";
import { InCartContext, MoveCardContext, ObjectRefContext } from "./App";
import type {cardProps} from './types'

export default function Card({ data,order }: cardProps) {
    const [displayInfo, setDisplayInfo] = useState<boolean>(false);
    const [position, setPosition] = useState({ x:0 , y:0 });
    const [dragOffset, setDragOffset] = useState({ x:0 , y:0 });
    const [isDragging, setIsDragging] = useState<boolean>(false);
    const cardRef = useRef<HTMLDivElement>(null);
    const wasDraggedRef = useRef<boolean>(false);
    const contextMoveCard = useContext(MoveCardContext);
    const ref = useContext(ObjectRefContext);
    const inCart = useContext(InCartContext);

    const moveCard = contextMoveCard?.moveCard;

    useEffect(() => {
        if (isDragging) {
            document.addEventListener('mousemove', handleMouseMove);
            document.addEventListener('mouseup', handleMouseUp);
        } 
        else {
            document.removeEventListener('mousemove', handleMouseMove);
            document.removeEventListener('mouseup', handleMouseUp);
        }

        return () => {
            document.removeEventListener('mousemove', handleMouseMove);
            document.removeEventListener('mouseup', handleMouseUp);
        };
    },[isDragging]);

    const handleMouseDown = (e: React.MouseEvent<HTMLDivElement>) => {
        const isInCart = !!inCart?.[data.id];
        if(isInCart) return;
        if(!cardRef.current) return;
        wasDraggedRef.current = false;
        const rect = cardRef.current?.getBoundingClientRect();
        setDragOffset({
            x: e.clientX - rect.left,
            y: e.clientY - rect.top
        });
        setPosition({
            x: e.clientX,
            y: e.clientY
        });
        setIsDragging(true);
    }

    const handleMouseMove = (e: MouseEvent) => {
        if(isDragging) {
            wasDraggedRef.current = true;
            setPosition({
                x: e.clientX,
                y: e.clientY
            });
        }
    }

    const handleMouseUp = () => {
        if (ref && ref.current && cardRef.current) {
            const cardRect = cardRef.current.getBoundingClientRect();
            const targetRect = ref.current.getBoundingClientRect();

            if (
                cardRect.left < targetRect.right &&
                cardRect.right > targetRect.left &&
                cardRect.top < targetRect.bottom &&
                cardRect.bottom > targetRect.top &&
                moveCard
            ) {
                moveCard(data);
            }
        }
        setIsDragging(false);
        setPosition({ x: 0, y: 0 });
        setDragOffset({ x: 0, y: 0 });
    }

    const handleClick = () => {
        if(!wasDraggedRef.current)
            setDisplayInfo(true);
        
    }

    return (
        <>
            <div className="cardClass" ref={cardRef} onClick={() => handleClick()} onMouseDown={(e) => handleMouseDown(e)}
                style={{
                    position: isDragging ? 'absolute' : 'relative',
                    left: position.x - dragOffset.x,
                    top: position.y - dragOffset.y,
                    cursor: isDragging ? 'grabbing' : 'pointer',
                    zIndex: isDragging ? 9999 : 'auto',
                    userSelect: isDragging ? 'none':'auto',
                    width: '242px',
                    height:'150px'
                }}>
                <h2>{data.name}</h2>
                <p>{data.description.length > 25 ? data.description.slice(0,25) + '...' : data.description}</p>
                <p>Price: ${data.price}</p>
                <br />
                <p>Stock: {data.quantity}</p>
                </div>

            {displayInfo && 
                (<div className="productOverlay">
                    <div className="productClass">
                    <h1>{data.name}</h1>
                    <button className="closeBtn" onClick={() => setDisplayInfo(false)}>×</button>
                    <p><strong>Description: </strong>{data.description}</p>
                    <p><strong>Price: </strong>${data.price}</p>
                    {order &&
                        <>
                        <p><strong>You bought: </strong>{order.amountBought}</p> 
                        </>
                    }
                    <p><strong>Stock: </strong>{data.quantity}</p>
                    {!inCart[data.id] && <button className="addToCartBtn" onClick={() =>{if(moveCard) moveCard(data);}}>Add to cart</button>}
                    {inCart[data.id] && <div className="optionButtons">
                        <button className="addToCartBtn" onClick={() =>{if(moveCard) moveCard(data,'products');}}>Out of cart</button>
                        <button className="addToCartBtn" onClick={() =>{if(moveCard) moveCard(data,'productsBuy');}}>Buy</button>
                        </div>}
                    </div>
                </div>)
            }
        </>
    );
}