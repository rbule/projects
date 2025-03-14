import numpy as np
import tensorflow as tf
from tensorflow.keras.datasets import mnist

def load_emnist():
    with np.load('C:/faks/projekti/MLDigits/mnist.npz') as data:
        x_train = data['x_train']
        y_train = data['y_train']
        x_test = data['x_test']
        y_test = data['y_test']

        x_train = x_train.astype('float32') / 255.0
        x_test = x_test.astype('float32') / 255.0

        x_train = x_train.reshape(x_train.shape[0], 28 * 28)
        x_test = x_test.reshape(x_test.shape[0], 28 * 28)

        y_train = tf.keras.utils.to_categorical(y_train, num_classes=10)
        y_test = tf.keras.utils.to_categorical(y_test, num_classes=10)

    return x_train, y_train, x_test, y_test

learning_rate = 0.01

def sigmoid(z):
    return 1 / (1 + np.exp(-np.clip(z, -15, 15)))

def initialize():
    #784 -> 256 -> 128 -> 10

    W1 = np.random.uniform(-0.1,0.1,(256,784))
    W2 = np.random.uniform(-0.1,0.1,(128,256))
    W3 = np.random.uniform(-0.1,0.1,(10,128))

    b1 = np.zeros((256,1))
    b2 = np.zeros((128,1))
    b3 = np.zeros((10,1))

    return W1, W2, W3, b1, b2, b3

def forward_propagation(A,W1,W2,W3,b1,b2,b3):
    A = A.reshape((-1,1))
    temp1 = np.dot(W1,A) + b1
    Z1 = sigmoid(temp1)

    temp2 = np.dot(W2,Z1) + b2
    Z2 = sigmoid(temp2)

    temp3 = np.dot(W3,Z2) + b3
    Z3 = np.exp(temp3 - np.max(temp3))
    Z3 = Z3 / np.sum(Z3)

    return Z1,Z2,Z3

def C(output,desired):
    return -np.sum(desired * np.log(output + 1e-8))

def back_propagation(A,y,W1,W2,W3,b1,b2,b3):
    Z1, Z2, Z3 = forward_propagation(A, W1, W2, W3, b1, b2, b3)
    
    dZ3 = Z3 - y.reshape(-1, 1) 
    
    dW3 = np.dot(dZ3, Z2.T)
    db3 = np.sum(dZ3, axis=1, keepdims=True)
    
    dZ2 = np.dot(W3.T, dZ3) * (Z2 * (1 - Z2))
    dW2 = np.dot(dZ2, Z1.T)
    db2 = np.sum(dZ2, axis=1, keepdims=True)
    
    dZ1 = np.dot(W2.T, dZ2) * (Z1 * (1 - Z1))
    dW1 = np.dot(dZ1, A.reshape(1, -1))
    db1 = np.sum(dZ1, axis=1, keepdims=True)

    W3 -= learning_rate * dW3
    b3 -= learning_rate * db3
    W2 -= learning_rate * dW2
    b2 -= learning_rate * db2
    W1 -= learning_rate * dW1
    b1 -= learning_rate * db1
    
    return W1,W2,W3,b1,b2,b3

def train(X_train,y_train,epochs,W1,W2,W3,b1,b2,b3):
    for e in range(epochs):
        print("Progress: ",(e)*2,"%")
        for i in range(60000):
            W1, W2, W3, b1, b2, b3 = back_propagation(X_train[i].T, y_train[i].T, W1, W2, W3, b1, b2, b3)

    return W1,W2,W3,b1,b2,b3

def evaluate_model(X_test, y_test, W1, W2, W3, b1, b2, b3):
    correct = 0
    total = X_test.shape[0]
    for i in range(total):
        _, _, A3 = forward_propagation(X_test[i], W1, W2, W3, b1, b2, b3)
        
        predicted_class = np.argmax(A3)
        true_class = np.argmax(y_test[i])
        
        if predicted_class == true_class:
            correct += 1
    
    accuracy = (correct / total) * 100
    print(f"Test Accuracy: {accuracy:.2f}%")
    return accuracy

x_train, y_train, x_test, y_test = load_emnist()
W1, W2, W3, b1, b2, b3 = initialize()
W1 ,W2 ,W3 ,b1, b2, b3 = train(x_train,y_train,50,W1,W2,W3,b1,b2,b3)
evaluate_model(x_test, y_test, W1, W2, W3, b1, b2, b3)


    
    
    

    
    
