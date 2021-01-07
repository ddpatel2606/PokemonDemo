package com.dixitpatel.pokemondemo.network

/**
 *  Authentication status
 */
sealed class AuthStatus
{
        object SUCCESS : AuthStatus()
        object ERROR : AuthStatus()
        object LOADING : AuthStatus()
}