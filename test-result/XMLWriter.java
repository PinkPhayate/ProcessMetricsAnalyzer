    public  static  final   class   XMLWriter
    {
        private         boolean       _vacuous;
        
        private         FileWriter   _fw;
        private         PrintWriter _pw;
        private         ArrayList<String>    _tagStack;

         */
        public  XMLWriter()
        {
            _vacuous = true;
         }

         */
        public  XMLWriter( File file )
            throws IOException
        {
            _vacuous = false;
            _fw = new FileWriter( file );
            _pw = new PrintWriter( _fw );
            _tagStack = new ArrayList<String>();
        }

        public  void    flush() throws IOException
        {
            if ( _vacuous ) { return; }
            
            _pw.flush();
            _fw.flush();
        }
        
        public  void    close() throws IOException
        {
            if ( _vacuous ) { return; }

            _pw.close();
            _fw.close();
        }

         */
        public void    writeEmptyTag( String tag )
            throws IOException
        {
            if ( _vacuous ) { return; }

            writeEmptyTag( tag, "" );
        }

         */
        public void    writeEmptyTag( String tag, String attributes )
            throws IOException
        {
            if ( _vacuous ) { return; }

            indent( );
            if ( attributes.length() >0)
                _pw.println( "<" + tag + " " + attributes + "/>");
            else
                _pw.println( "<" + tag + "/>");
        }

         */
        public void    beginTag( String tag )
            throws IOException
        {
            if ( _vacuous ) { return; }

            beginTag( tag, "" );
        }

         */
        public void    beginTag( String tag, String attributes )
            throws IOException
        {
            if ( _vacuous ) { return; }

            indent();
            if (attributes.length() > 0)
                _pw.println( "<" + tag + " " + attributes + ">");
            else
                _pw.println( "<" + tag + ">");

            _tagStack.add( tag );
        }

         */
        public void    endTag()
            throws IOException
        {
            if ( _vacuous ) { return; }

            String  tag = (String) _tagStack.remove( _tagStack.size() -1 );
        
            indent();

            _pw.println( "</" + tag + ">");
        }

         */
        public void    writeTextElement( String tag, String text )
            throws IOException
        {
            if ( _vacuous ) { return; }

            writeTextElement( tag, "", text );
        }

         */
        public void    writeTextElement( String tag, String attributes, String text )
            throws IOException
        {
            if ( _vacuous ) { return; }

            indent();
            if ( attributes.length() > 0 )
                _pw.print( "<" + tag + " " + attributes + ">");
            else
                _pw.print( "<" + tag + ">");
            _pw.print( text );
            _pw.println( "</" + tag + ">");
        }

         */
        public void    indent()
            throws IOException
        {
            if ( _vacuous ) { return; }

            int     tabCount = _tagStack.size();

            for ( int i = 0; i < tabCount; i++ ) { _pw.write( TAB_STOP ); }
        }

         */
        public void    println( String text )
            throws IOException
        {
            if ( _vacuous ) { return; }

            _pw.println( text );
        }

    }
