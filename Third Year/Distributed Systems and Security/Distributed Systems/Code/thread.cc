#include "ns3/fatal-error.h"
#include "thread.h"
#include "ns3/log.h"
#include <cstring>
 
namespace ns3 {
 
NS_LOG_COMPONENT_DEFINE ("Thread");
// NS_OBJECT_ENSURE_REGISTERED (Thread);
 
// #ifdef HAVE_PTHREAD_H
 
SystemThread::SystemThread (Callback<void> callback)
   : m_callback (callback)
{
   NS_LOG_FUNCTION (this << &callback);
}
 
SystemThread::~SystemThread()
{
   NS_LOG_FUNCTION (this);
}
 
void
SystemThread::Start (void)
{
   NS_LOG_FUNCTION (this);
 
   int rc = pthread_create (&m_thread, NULL, &SystemThread::DoRun,
                            (void *)this);
 
   if (rc) 
     {
       NS_FATAL_ERROR ("pthread_create failed: " << rc << "=\"" << 
                       strerror (rc) << "\".");
     }
}
 
void
SystemThread::Join (void)
{
   NS_LOG_FUNCTION (this);
 
   void *thread_return;
   int rc = pthread_join (m_thread, &thread_return);
   if (rc) 
     {
       NS_FATAL_ERROR ("pthread_join failed: " << rc << "=\"" << 
                       strerror (rc) << "\".");
     }
}
 
void *
SystemThread::DoRun (void *arg)
{
   NS_LOG_FUNCTION (arg);
 
   SystemThread *self = static_cast<SystemThread *> (arg);
   self->m_callback ();
 
   return 0;
}
SystemThread::ThreadId 
SystemThread::Self (void)
{
   NS_LOG_FUNCTION_NOARGS ();
   return pthread_self ();
}
 
bool 
SystemThread::Equals (SystemThread::ThreadId id)
{
   NS_LOG_FUNCTION (id);
   return (pthread_equal (pthread_self (), id) != 0);
}
 
// #endif
 
}