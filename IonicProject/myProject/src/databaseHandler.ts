import {openDB} from 'idb'
import { trip } from './models/Trip'

const DATABASE_NAME = "TripDB"

initDB().then(()=>{
    console.log("Database initialized complete!")
})

export const insertTrip = async (tripInfo:trip)=>{
    const db = await openDB(DATABASE_NAME,1)
    const key = await db.put("trips",tripInfo)
    console.log("inserted trip "+ key)
}

export const getAllTrip =async () => {
    const db = await openDB(DATABASE_NAME,1)
    return await db.getAll("trips")
}

export const getTripById =async (id:number) => {
    const db = await openDB(DATABASE_NAME,1)
    return await db.get("trips",id)
}

export const updateTrip = async (trip : any, id: number) => {
    const db = await openDB(DATABASE_NAME,1)
    return await db.put("trips", trip, id)
}

export const deleteTripById = async (id: number) =>{
    let isdeleted = false
    const db = await openDB(DATABASE_NAME,1)
    await db.delete("trips", id).then(() => {
        isdeleted = true;
    })
    
    return isdeleted;
}

export const clearDB = async () => {
    indexedDB.deleteDatabase(DATABASE_NAME)
    alert('success')
    return initDB()
}




async function initDB() {
    const db = await openDB(DATABASE_NAME,1,{
        upgrade(db){
            const store = db.createObjectStore('trips',{
                keyPath: 'id',
                autoIncrement:true
            })
        }
    })
}