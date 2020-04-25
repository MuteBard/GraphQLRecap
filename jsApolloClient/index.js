import ApolloBoost, { gql } from 'apollo-boost'

import getClient from './getClient'


const client = getClient()


const MutationExample = gql`
mutation {
    catchBug {
        username
        pocket {
            bug {
                bugId
                name
                bells
                availability
            }
        }
    }
}  
`
const QueryExample = gql`
query{
    getBugBaseOnMonth(month : "JUL")    
}
`

const SubscriptionExample = gql`
subscription {
    count
}
`


client.mutate({
    mutation : MutationExample
}).then(resp => {
    console.log(resp.data)
})


client.query({
    query : QueryExample
}).then(resp => {
    console.log(resp.data)
})

client.subscribe({ query: SubscriptionExample}).subscribe({
    next(response) {
        console.log(response.data)
    }
})
