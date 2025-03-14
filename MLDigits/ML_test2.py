import numpy as np
import tensorflow as tf
from tensorflow.keras.datasets import mnist

learning_rate = 0.001
layers = int(input("Layers: "))
neurons = []
for i in range(layers):
    print("Neurons in",i+1,"-th layer:")
    neurons.append(int(input()))
epochs = int(input("Epochs: "))
batch_size = int(input("Batch size: "))

def load_data():
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

def sigmoid(Z):
    return 1 / (1 + np.exp(-np.clip(Z, -15, 15)))

def softmax(Z):
    Z_max = np.max(Z, axis=0, keepdims=True)
    exp_Z = np.exp(Z - Z_max)
    return exp_Z / np.sum(exp_Z, axis=0, keepdims=True)

def C(output,desired):
    value = 0
    for i in range(output.shape[0]):
        value -= output[i] * np.log(desired[i])
    return value

def initialize(layers,neurons):
    weights = []
    biases = []
    
    for i in range(layers-1):
        weights.append(np.random.uniform(-0.1,0.1,(neurons[i+1],neurons[i])))
        biases.append(np.zeros((neurons[i+1],1)))

    return weights , biases

def forward_propagation(A,W,b,layers):
    Z = []
    A_temp = A
    
    for i in range(layers-2):
        Zi = np.dot(W[i] , A_temp) + b[i]
        A_temp = sigmoid(Zi)
        Z.append(A_temp)

    Zn = np.dot(W[layers-2],A_temp) + b[layers-2]
    A_temp = softmax(Zn)
    Z.append(A_temp)
    return Z

def back_propagation(A,Y,W,b,layers,batch_size):
    Z = forward_propagation(A,W,b,layers)
    dCdZ = []

    dCdZ.append((Z[layers-2] - Y))
    dCdb = np.sum(dCdZ[0], axis=1, keepdims=True)
        
    W[layers-2] -= learning_rate/batch_size * dCdZ[0] @ Z[layers-3].T
    b[layers-2] -= learning_rate/batch_size * dCdb
    
    for i in range(layers-3,0,-1):
        j=i-layers+3
        dCdZ.append(W[i+1].T @ dCdZ[j])
        W[i] -= learning_rate/batch_size * dCdZ[j+1] @ Z[i-1].T
        dCdb = np.sum(dCdZ[j+1], axis=1, keepdims=True)
        b[i] -= learning_rate/batch_size * dCdb
    
    return W, b

def train(W,b,x_train,y_train,epochs,batch_size,layers):
    for epoch in range(epochs):
        print("Epoch:",epoch, "/",epochs)
        for i in range(60000//batch_size-1):
            W, b = back_propagation(x_train[batch_size*i:batch_size*(i+1)].T,y_train[batch_size*i:batch_size*(i+1)].T,W,b,layers,batch_size)
    return W, b

def test(W, b, x_test, y_test,layers):
    Z = forward_propagation(x_test.T, W, b, layers)
    output = Z[-1]
    
    predicted = np.argmax(output, axis=0)
    true = np.argmax(y_test, axis=1)
    
    correct = np.sum(predicted == true)
    
    accuracy = correct / y_test.shape[0]
    return accuracy

x_train,y_train,x_test,y_test = load_data()
W, b = initialize(layers,neurons)
W, b = train(W,b,x_train,y_train,epochs,batch_size,layers)
print(test(W,b,x_test,y_test,4)*100)


    
