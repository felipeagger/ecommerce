package main

import (
	"fmt"
	"os"
	"time"

	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/credentials"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/dynamodb"
	"github.com/aws/aws-sdk-go/service/dynamodb/dynamodbattribute"
	"github.com/aws/aws-sdk-go/service/dynamodb/expression"
	"github.com/dgrijalva/jwt-go"
)

func getDynamoSession() *dynamodb.DynamoDB {

	config := &aws.Config{
		Region:      aws.String("sa-east-1"),
		Credentials: credentials.NewSharedCredentials("", "default"),
		//Endpoint: aws.String("http://localhost:8000"),
	}

	sess := session.Must(session.NewSession(config))

	svc := dynamodb.New(sess)

	fmt.Println("AWS Session initialized...")

	//if err != nil {
	//	fmt.Println("Error initializing : ", err)
	//	panic("Client fail ")
	//}

	return svc
}

func (h *handlers) findUserByUsername(Username string) register {

	user := register{}

	if Username != "" {

		filt := expression.Name("username").Equal(expression.Value(Username))
		expr, _ := expression.NewBuilder().WithFilter(filt).Build()

		params := &dynamodb.ScanInput{
			ExpressionAttributeNames:  expr.Names(),
			ExpressionAttributeValues: expr.Values(),
			FilterExpression:          expr.Filter(),
			TableName:                 aws.String("users"),
		}

		result, _ := h.dynamo.Scan(params)

		if len(result.Items) > 0 {
			dynamodbattribute.UnmarshalMap(result.Items[0], &user)
		}
	}

	return user
}

func createToken(userid string) (string, error) {
	var err error

	//Creating Access Token
	os.Setenv("ACCESS_SECRET", "jdnfksdms546dsa645d0as5fksd")

	atClaims := jwt.MapClaims{}
	atClaims["authorized"] = true
	atClaims["user_id"] = userid
	atClaims["exp"] = time.Now().Add(time.Minute * 15).Unix()
	at := jwt.NewWithClaims(jwt.SigningMethodHS256, atClaims)
	token, err := at.SignedString([]byte(os.Getenv("ACCESS_SECRET")))
	if err != nil {
		return "", err
	}
	return token, nil
}

func tokenValid(tokenStr string) error {
	token, err := verifyToken(tokenStr)
	if err != nil {
		return err
	}
	if _, ok := token.Claims.(jwt.Claims); !ok && !token.Valid {
		return err
	}
	return nil
}

func verifyToken(tokenStr string) (*jwt.Token, error) {

	token, err := jwt.Parse(tokenStr, func(token *jwt.Token) (interface{}, error) {

		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return []byte(os.Getenv("ACCESS_SECRET")), nil

	})

	if err != nil {
		return nil, err
	}
	return token, nil
}
